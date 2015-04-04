package com.example.stven_king.tzx2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stven_king.tzx2048.dialog.NiftyDialogBuilder;
import com.example.stven_king.tzx2048.dialog.effects.EffectsInfo;
import com.example.stven_king.tzx2048.model.MyToast;
import com.example.stven_king.tzx2048.utils.BestScode;
import com.example.stven_king.tzx2048.R;
import com.example.stven_king.tzx2048.model.ViewCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by STVEN_KING on 2015/1/15.
 */
public class MainActivity extends Activity implements OnGestureListener,ViewCell.ViewCellListener {
    private static final int NEW_GAME = 101;
    private static final int BACK_GAME = 102;
    private static int GAME = NEW_GAME;
    //显示屏幕的宽高
    private int width;
    private int height;

    //每个移动方框View的宽和高
    private int iwidth;
    private int iheight;

    //整个游戏主界面的布局
    private LinearLayout centerlayout;

    //4*4的游戏方块
    private List<ViewCell> lists = new ArrayList<ViewCell>();
    private int[][] arrays = new int[4][4];
    //只能返回3次
    private int[][] lastarrays = new int[4][4];
    private int[][] lastarrays2 = new int[4][4];
    private int[][] lastarrays3 = new int[4][4];

    private enum GoBackStep{
        one,two,three,zero
    }

    private GoBackStep goBackStep;

    //产生随机数
    private Random random = new Random();
    //手势检测
    private GestureDetector gd;
    //检测是否能移动
    private boolean flag_move = false;
    //显示当前成绩
    private TextView tv_currenScore;
    //显示历史纪录的最高成绩
    private TextView tv_bestScore;
    //当前成绩
    private int current_score = 0;
    //最高成绩
    private int best_score = 0;
    //当前金币数量
    private int goldsNum = 0;

    //private BestScode record;

    private SoundPool soundPool=null;
    //分别纪录移动方块和合并方块的声音
    private int sound1,sound2;

    boolean isFail=false;//有相同的合并

    //祈祷
    private ImageButton qdImageButton;
    //交换
    private ImageButton jhImageButton;
    //锤子
    private ImageButton czImageButton;
    //返回上一步
    private ImageButton fhImageButton;
    //暂停
    private ImageButton stopButton;

    //判断用户是否退出游戏
    private boolean isExit;

    //用户点击位置
    private int New_X,New_Y;

    //游戏道具
    private final static int QIDAO_PRESS = 101;
    private final static int JIAOHUAN_PRESS = 102;
    private final static int CHUIZI_PRESS = 103;
    private final static int FANHUI_PRESS = 103;

    //游戏工具
    private enum GameTool_Press{
        qidao,jiaohuan,chuizi,fanhui,empty
    }
    private static GameTool_Press gameTool_Press;

    //是否进行交换的第二次点击
    private boolean JiaoHuan_Twice =false;
    private ViewCell First;
    private ViewCell Second;

    //灵敏度（移动间距是否大于40，如果小于40视作没有移动）
    private final int FLING_Min_DISTANCE =40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display dp = wm.getDefaultDisplay();

        //获取屏幕的宽和高
        width = dp.getWidth();
        height = dp.getHeight();
        iwidth=width/4;
        iheight=iwidth;

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sound2=soundPool.load(getBaseContext(), R.raw.huiqi, 1);
        sound1=soundPool.load(getBaseContext(), R.raw.chuizi, 1);

        for (int[] a:lastarrays){
            for (int b:a){
                b=0;
            }
        }
        for (int[] a:lastarrays2){
            for (int b:a){
                b=0;
            }
        }
        for (int[] a:lastarrays3){
            for (int b:a){
                b=0;
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        centerlayout = new LinearLayout(this);
        centerlayout.setLayoutParams(params);
        centerlayout.setOrientation(LinearLayout.VERTICAL);
        //设置屏幕主色调为天亮蓝颜色
        centerlayout.setBackgroundResource(R.color.bg);

        //纪录最高成绩和当前成绩
        View scode =View.inflate(this, R.layout.main, null);
        stopButton = (ImageButton) scode.findViewById(R.id.stopgame);
        stopButton.setOnClickListener(new OnClickListenerStop());
        //工具菜单栏
        View menu = View.inflate(this, R.layout.menu, null);
        //要挑战的目标成绩
        View goal = View.inflate(this, R.layout.goal, null);

        //分别将每个View加入到主View中
        centerlayout.addView(scode);
        centerlayout.addView(goal);
        centerlayout.addView(getMainLayout());
        centerlayout.addView(menu);
        setContentView(centerlayout);

        //祈祷：将当前的数字*2
        qdImageButton = (ImageButton) this.findViewById(R.id.qidao);
        qdImageButton.setOnClickListener(new OnClickListenerQiDao());

        //交换：交换两个数字
        jhImageButton = (ImageButton) this.findViewById(R.id.jiaohuan);
        jhImageButton.setOnClickListener(new OnClickListenerJiaoHuan());

        //锤子将当前数字取消
        czImageButton = (ImageButton) this.findViewById(R.id.chuizi);
        czImageButton.setOnClickListener(new OnClickListenerChuiZi());

        //返回：返回到上一个状态
        fhImageButton = (ImageButton) this.findViewById(R.id.fanhui);
        fhImageButton.setOnClickListener(new OnClickListenerFanHui());

        tv_currenScore = (TextView) findViewById(R.id.tv_currenScore);
        tv_bestScore = (TextView) findViewById(R.id.tv_bestScore);

        //设置自定义的字体
        tv_currenScore.setTypeface(Typeface.createFromAsset(getAssets(),"font/Non_mainstream_digital_fonts.ttf"));
        //tv_bestScore.setTypeface(Typeface.createFromAsset(getAssets(),"font/Bank_card_number_font.ttf"));
        tv_bestScore.setTypeface(Typeface.createFromAsset(getAssets(),"font/Non_mainstream_digital_fonts.ttf"));

        best_score = BestScode.getInstance(this).getBestScode();
        tv_bestScore.setText(best_score + "");
        goldsNum = BestScode.getInstance(this).getGolds();
        gd = new GestureDetector(this,this);

        //设定没有点击工具
        gameTool_Press=GameTool_Press.empty;

        //设定返回0步
        goBackStep = GoBackStep.zero;

        //产生数字游戏开始
        random2Or4();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MainActivity","onTouchEvent");
        return gd.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        if (getFillZero()){
            random2Or4();
        }
        Log.v("MainActivity","onDown");
        //获取当前的坐标
        New_X = (int) event.getRawX();
        New_Y = (int) event.getRawY();

        //点击交换后获取的第二个非0数字
        if(JiaoHuan_Twice){
            int num2 = 0;
            while(num2 == 0){
                Second = JiaoHuan();
                num2 = Second.getNumber();
            }
            Second.setNumber(First.getNumber());
            First.setNumber(num2);
            JiaoHuan_Twice = false;
        }

        //判断是否点击工具栏
        if(gameTool_Press == GameTool_Press.empty)
            return false;


        switch (gameTool_Press) {
            //点击祈祷
            case qidao:
                QiDao();
                break;
            //点击交换
            case jiaohuan:
                int num1 = 0;
                while(num1 == 0){
                    First = JiaoHuan();
                    num1 = First.getNumber();
                }
                JiaoHuan_Twice = true;
                gameTool_Press = GameTool_Press.empty;
                break;
            //点击锤子
            case chuizi:
                ChuiZi();
                if (getFillZero()) {
                    random2Or4();
                }
                break;
            //点击返回
            case fanhui:
                gameTool_Press = GameTool_Press.empty;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        if (getFillZero()){
            random2Or4();
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (getFillZero()){
            random2Or4();
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (getFillZero()){
            random2Or4();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (getFillZero()){
            random2Or4();
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("MainActivity","onFling");
        float X=e2.getX()-e1.getX();
        float Y=e2.getY()-e1.getY();

        //向右滑动
        if(X>FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
            toRight();
            //向左滑动
        }else if(X<-FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
            toLeft();
            //向下滑动
        }else if(Y>FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
            toDown();
            //向上滑动
        }else if(Y<-FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
            toUp();
        }
        return false;
    }

    /**
     * TODO返回主布局
     * */
    private View getMainLayout() {
        LinearLayout colLayout = new LinearLayout(this);
        colLayout.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<4;i++){
            colLayout.addView(getRowLayout());
        }
        return colLayout;
    }

    /**
     * TODO自定义4*4的16个View
     * */
    private View getRowLayout() {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<4;i++){
            ViewCell viewcell = new ViewCell(this,iwidth,iheight);
            rowLayout.addView(viewcell);
            lists.add(viewcell);
        }
        return rowLayout;
    }

    private class OnClickListenerStop implements OnClickListener{

        @Override
        public void onClick(View v) {
            ShowDialog(getResources().getString(R.string.go_back),getResources().getString(R.string.calcel_button),BACK_GAME);
        }
    }

    /**
     * 点击祈祷进行监听
     * */
    private class OnClickListenerQiDao implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(goldsNum < 10){
                MyToast.maketext(MainActivity.this,getResources().getString(R.string.qidao_can_not),MyToast.TYPE_FAIL).show();
                qdImageButton.setClickable(false);
                qdImageButton.setBackgroundResource(R.drawable.qidao2);
            } else {
                goldsNum -= 10;
                BestScode.getInstance(MainActivity.this).cutGolds(10);
                gameTool_Press = GameTool_Press.qidao;
            }
        }

    }

    /**
     * 选中方块进行祈祷，将选中的非0数字*2
     * */
    private void QiDao(){

        int theNumber=0;
        for(int x=0;x<arrays.length;x++){
            for(int y=0;y<arrays[x].length;y++){
                ViewCell view = lists.get(x*4+y);
                if(view.IsClick(New_X, New_Y)){
                    theNumber = view.getNumber();
                    if(theNumber != 0){
                        view.setViewCellListener(this);
                        view.UseQiDao();
                        gameTool_Press = GameTool_Press.empty;
                        IsWin();
                    }else {
                        return;
                    }
                }

            }
        }
    }

    /**
     * 点击交换进行监听
     * */
    private class OnClickListenerJiaoHuan implements OnClickListener{

        @Override
        public void onClick(View v) {
            if (goldsNum < 10) {
                //Toast.makeText(MainActivity.this,"金币不足10枚，无法使用该道具",Toast.LENGTH_SHORT).show();
                MyToast.maketext(MainActivity.this,getResources().getString(R.string.qidao_can_not),MyToast.TYPE_FAIL).show();
                jhImageButton.setClickable(false);
                jhImageButton.setBackgroundResource(R.drawable.jiaohuan2);
            } else {
                goldsNum -= 10;
                BestScode.getInstance(MainActivity.this).cutGolds(10);
                gameTool_Press = GameTool_Press.jiaohuan;
            }
        }

    }

    /**
     * @return 返回选中的方块
     * */
    private ViewCell JiaoHuan(){

        ViewCell cell = new ViewCell(this);
        for(int x=0;x<arrays.length;x++){
            for(int y=0;y<arrays[x].length;y++){
                ViewCell view = lists.get(x*4+y);
                if(view.IsClick(New_X, New_Y)){
                    return view;
                }

            }
        }
        return cell;
    }

    /**
     * 点击锤子进行监听
     * */
    private class OnClickListenerChuiZi implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(goldsNum < 5){
                //Toast.makeText(MainActivity.this,"金币不足5枚，无法使用该道具",Toast.LENGTH_SHORT).show();
                MyToast.maketext(MainActivity.this,getResources().getString(R.string.chuizi_can_not),MyToast.TYPE_FAIL).show();
                czImageButton.setClickable(false);
                czImageButton.setBackgroundResource(R.drawable.chuizi2);
            } else {
                goldsNum -= 5;
                BestScode.getInstance(MainActivity.this).cutGolds(5);
                gameTool_Press = GameTool_Press.chuizi;
            }
        }

    }

    /**
     * 点击使用锤子，将选中的数字消除
     * */
    private void ChuiZi(){

        int theNumber=0;
        for(int x=0;x<arrays.length;x++){
            for(int y=0;y<arrays[x].length;y++){
                ViewCell view = lists.get(x*4+y);
                if(view.IsClick(New_X, New_Y)){
                    theNumber = view.getNumber();
                    if(theNumber != 0){
                        view.setViewCellListener(this);
//                        view.setNumber(0);
                        view.UseChuiZi();
                        gameTool_Press = GameTool_Press.empty;
                    }else {
                        return;
                    }
                }

            }
        }
    }

    /**
     * 点击返回进行监听
     * */
    private class OnClickListenerFanHui implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(goldsNum < 5){
                MyToast.maketext(MainActivity.this,getResources().getString(R.string.chuizi_can_not),MyToast.TYPE_FAIL).show();
                fhImageButton.setClickable(false);
                fhImageButton.setBackgroundResource(R.drawable.fanhui2);
            } else {
                goldsNum -= 5;
                BestScode.getInstance(MainActivity.this).cutGolds(5);
                gameTool_Press = GameTool_Press.fanhui;
                if (goBackStep == GoBackStep.zero) {
                    GoBackDraw();
                    goBackStep = goBackStep.one;
                } else if (goBackStep == GoBackStep.one) {
                    GoBackDraw2();
                    goBackStep = GoBackStep.two;
                } else if (goBackStep == GoBackStep.two) {
                    GoBackDraw3();
                    goBackStep = GoBackStep.three;
                    fhImageButton.setClickable(false);
                    fhImageButton.setBackgroundResource(R.drawable.fanhui2);
                } else if (goBackStep == GoBackStep.three) {
                    MyToast.maketext(MainActivity.this,getResources().getString(R.string.fanhui_can_not),MyToast.TYPE_FAIL).show();
                }
            }
        }

    }

    /**
     * TODO随机产生2或4
     * */
    private void random2Or4() {
        Log.d("my","random2O4");
        //判断是否出现数字2048，是：结束游戏，否继续游戏
        if (IsWin()) {
            //TODO 没有实现此功能
        }
        //判断是否充满非0数字，是：游戏继续，否：此局游戏结束
        if (!getFillAll()) {

            //随机在4*4的某个位置产生新的数字
            int row = random.nextInt(4);
            int col = random.nextInt(4);

            //获取这个位置的信息
            ViewCell randomview = lists.get(row * 4 + col);

            //判断这个位置有没有数字，如果有：重新产生随机数
            if (randomview.getNumber() == 0) {
                //随机生成数字2或者4
                int n2Or4 = (random.nextInt(2) + 1) * 2;
                randomview.setNumber(n2Or4);
                //初始化isFail表示当前没有合并数字
                isFail = true;
                //判断当前是否将所有的view填充完而且没有能合并的数字
                if (getFillAll() && notAdd()) {
                    ShowDialog(getResources().getString(R.string.ok_button),getResources().getString(R.string.calcel_button),NEW_GAME);
                }
            }else {
                random2Or4();
            }
        }
    }


    /**
     * 判断是否充满非0数字
     * @return true 所有的方块都被非0的数字填充
     */
    private boolean getFillAll(){
        boolean isFill=true;
        markNumber();
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(arrays[i][j]==0){
                    isFill=false;
                }
            }
        }
        return isFill;
    }

    /**
     * 判断是否充满0数字
     *
     * @return true 所有的方块都被0的数字填充
     */
    private boolean getFillZero() {
        boolean isFill = true;
        markNumber();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (arrays[i][j] != 0) {
                    isFill = false;
                }
            }
        }
        return isFill;
    }

    /**
     * 判断是否达到2048
     *
     * @return true 拼出2048数字
     */
    private boolean IsWin() {
        boolean isFail = false;
        markNumber();
        for (int i = 0; i < 4 && !isFail; i++) {
            for (int j = 0; j < 4; j++) {
                if (arrays[i][j] == 2048) {
                    isFail = true;
                    startActivity(new Intent(MainActivity.this, GameBestActivity.class));
                    onDestroy();
                    break;
                }
            }
        }
        return isFail;
    }

    /**
     * 判断是否有相邻数字是否相等；
     * @return true ：有（非0）相邻数字相等，即：可以进行移动
     * 遍历每个数字，分别判断这个数字的右边和下边的连个数字是否与这个数字相同
     */
    private boolean notAdd(){
        markNumber();
        boolean notadd = true;
        for(int i=0;i<4;i++){
            for(int j=0;j<3;j++){
                if(arrays[i][j]==arrays[i][j+1]&&arrays[i][j]!=0){
                    notadd = false;
                }else if(arrays[j][i]==arrays[j+1][i]&&arrays[j][i]!=0){
                    notadd = false;
                }
            }
        }
        return notadd;
    }

    /**
     * 记录数字方格表中的数字。
     */
    private void markNumber() {

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                ViewCell view =lists.get(i*4+j);
                arrays[i][j] = view.getNumber();
            }
        }
    }

    /**
     * 返回上一步
     */
    private void GoBackDraw() {
        for(int x=0;x<lastarrays.length;x++){
            for(int y=0;y<lastarrays[x].length;y++){
                ViewCell view = lists.get(x*4+y);
                view.setNumber(lastarrays[x][y]);
            }
        }
        if(getFillZero()){
            random2Or4();
        }
    }
    /**
     * 返回上两步
     */
    private void GoBackDraw2() {
        for(int x=0;x<lastarrays2.length;x++){
            for(int y=0;y<lastarrays2[x].length;y++){
                ViewCell view = lists.get(x*4+y);
                view.setNumber(lastarrays2[x][y]);
            }
        }
        if(getFillZero()){
            random2Or4();
        }
    }
    /**
     * 返回上三步
     */
    private void GoBackDraw3() {
        for(int x=0;x<lastarrays3.length;x++){
            for(int y=0;y<lastarrays3[x].length;y++){
                ViewCell view = lists.get(x*4+y);
                view.setNumber(lastarrays3[x][y]);
            }
        }
        if(getFillZero()){
            random2Or4();
        }
    }

    /**
     * 判断是否为最佳成绩如果是则记录
     * */
    private void isBest(int score){
        if(score > best_score){
            BestScode.getInstance(MainActivity.this).setBestScode(score);
            best_score = score;
            tv_bestScore.setText(best_score + "");
        }
    }

    /**
     * 纪录上次移动痕迹
     * */
    public void GoLast(){

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                lastarrays3[i][j] = lastarrays2[i][j];
            }
        }
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                lastarrays2[i][j] = lastarrays[i][j];
            }
        }
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                lastarrays[i][j] = arrays[i][j];
            }
        }
    }

    /**
     * 向上划动
     */
    private void toUp() {
        GoBack();
        markNumber();
        GoLast();
        orderUp();
        //合并相同数据
        int addscode = 0;
        for(int p =0;p<4;p++){
            for(int q=0;q<3;q++){
                if(arrays[q][p]==arrays[q+1][p]&&arrays[q][p]!=0){
                    soundPool.play(sound1, 1f, 1f, 1, 0, 1f);
                    isFail=false;//有相同的合并
                    flag_move = true;
                    arrays[q][p]+=arrays[q+1][p];
                    addscode+=arrays[q][p];
                    arrays[q+1][p]=0;
                    q++;
                }
            }
        }
        orderUp();
        move2draw();
        current_score+=addscode;
        //比较，写入最高分数
        isBest(current_score);
        tv_currenScore.setText(current_score+"");
    }

    /**
     * 向下划动
     */
    private void toDown() {
        GoBack();
        markNumber();
        GoLast();
        //逐一排列
        orderDown();
        //合并相同数据
        int addscode = 0;
        for(int p=0;p<4;p++){
            for(int q=3;q>0;q--){
                if(arrays[q][p]==arrays[q-1][p]&&arrays[q][p]!=0){
                    soundPool.play(sound1, 1f, 1f, 1, 0, 1f);
                    isFail=false;//有相同的合并
                    flag_move = true;
                    arrays[q][p]+=arrays[q-1][p];
                    addscode+=arrays[q][p];
                    arrays[q-1][p]=0;
                    q--;
                }
            }
        }
        orderDown();
        move2draw();
        current_score+=addscode;
        //比较，写入最高分数
        isBest(current_score);
        tv_currenScore.setText(current_score+"");
    }

    /**
     * 向左划动
     */
    private void toLeft() {
        GoBack();
        markNumber();
        GoLast();
        orderLeft();
        //合并相等数据
        int addscode = 0;
        for(int p =0;p<4;p++){
            for(int q=0;q<3;q++){
                if(arrays[p][q]==arrays[p][q+1]&&arrays[p][q]!=0){
                    soundPool.play(sound1, 1f, 1f, 1, 0, 1f);
                    isFail=false;//有相同的合并
                    flag_move = true;
                    arrays[p][q]+=arrays[p][q+1];
                    addscode+=arrays[p][q];
                    arrays[p][q+1]=0;
                    q++;
                }
            }
        }
        orderLeft();
        move2draw();
        current_score+=addscode;
        //比较，写入最高分数
        isBest(current_score);
        tv_currenScore.setText(current_score+"");
    }


    /**
     * 向右划动
     */
    private void toRight() {
        GoBack();
        markNumber();
        GoLast();
        orderRight();
        //合并相同数据
        int addscode = 0;
        for(int p =0;p<4;p++){
            for(int q=0;q<3;q++){
                if(arrays[p][q]==arrays[p][q+1]&&arrays[p][q]!=0){
                    soundPool.play(sound1, 1f, 1f, 1, 0, 1f);
                    isFail=false;//有相同的合并
                    flag_move = true;
                    arrays[p][q]+=arrays[p][q+1];
                    addscode+=arrays[p][q];
                    arrays[p][q+1]=0;
                    q++;
                }
            }
        }
        orderRight();
        move2draw();
        current_score+=addscode;
        //比较，写入最高分数
        isBest(current_score);
        tv_currenScore.setText(current_score+"");
    }

    /**
     * 向上划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderUp() {
        //逐一排列
        for(int n=0;n<4;n++){
            //冒泡排序法
            for(int m=0;m<4;m++){
                for(int i=m+1;i<4;i++){
                    if(arrays[m][n]==0&&arrays[i][n]!=0){
                        flag_move = true;
                        arrays[m][n]=arrays[i][n];
                        arrays[i][n]=0;
                    }
                }
            }
        }
    }

    /**
     * 向下划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderDown() {
        for(int n=0;n<4;n++){
            //冒泡排序法
            for(int m=3;m>=0;m--){
                for(int i=m-1;i>=0;i--){
                    if(arrays[m][n]==0&&arrays[i][n]!=0){
                        flag_move = true;
                        arrays[m][n]=arrays[i][n];
                        arrays[i][n]=0;
                    }
                }
            }
        }
    }

    /**
     * 向左划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderLeft() {
        //逐一排列
        for(int n=0;n<4;n++){
            //冒泡排序法
            for(int m=0;m<4;m++){
                for(int i=m+1;i<4;i++){
                    if(arrays[n][m]==0&&arrays[n][i]!=0){
                        flag_move = true;
                        arrays[n][m]=arrays[n][i];
                        arrays[n][i]=0;
                    }
                }
            }
        }
    }

    /**
     * 向右划动，方格排列，非0靠前，0靠后，如0、2、0、4 ——>2、4、0、0
     */
    private void orderRight() {
        //逐一排列
        for(int n=0;n<4;n++){
            //冒泡排序法
            for(int m=3;m>=0;m--){
                for(int i=m-1;i>=0;i--){
                    if(arrays[n][m]==0&&arrays[n][i]!=0){
                        flag_move = true;
                        arrays[n][m]=arrays[n][i];
                        arrays[n][i]=0;
                    }
                }
            }
        }
    }

    /**
     * /有移动数字方格就重画数字方格表并随机产生2或4数字方格，否则，不重画和产生数字方格。
     */
    private void move2draw() {
        if(flag_move){//判断是否有移动
            //重画方格
            if(isFail){
                soundPool.play(sound2, 1f, 1f, 1, 0, 1f);
            }
            for(int x=0;x<arrays.length;x++){
                for(int y=0;y<arrays[x].length;y++){
                    ViewCell view = lists.get(x*4+y);
                    view.setNumber(arrays[x][y]);
                }
            }
            random2Or4();
            flag_move = false;
        }
    }

    /**
     * 纪录行走的步数
     * */
    private void GoBack(){
        if(goBackStep == GoBackStep.one){
            goBackStep = GoBackStep.zero;
        } else if (goBackStep == GoBackStep.two) {
            goBackStep = GoBackStep.one;
        } else if (goBackStep == GoBackStep.three) {
            goBackStep = GoBackStep.two;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit(){
        if(!isExit) {
            isExit = true;
            //Toast.makeText(getApplicationContext(), "Press again to exit the program", Toast.LENGTH_SHORT).show();
            MyToast.maketext(MainActivity.this,getResources().getString(R.string.press_exit_again),MyToast.TYPE_FAIL).show();
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case 1:
                default:
                    break;
            }
        }
    };
    /**
     * 回掉动画结束状态
     * */
    @Override
    public void onFinished() {
        if(getFillZero()){
            random2Or4();
        } else if (IsWin()){

        }
    }
    /**
     * 显示Dialog
     * */
    public void ShowDialog(String ok,String cancel,int what){
        GAME = what;
        String message = getResources().getString(R.string.dialog_message2);
        if (GAME == NEW_GAME){
            message = getResources().getString(R.string.dialog_message1);
        }
        NiftyDialogBuilder.getInstance(MainActivity.this)
                .withTitle(getResources().getString(R.string.dialog_title))
                .withTitleColor(getResources().getColor(R.color.dialog_title))
                .withDividerColor(getResources().getColor(R.color.dialog_divider))
                .withMessage(message)
                .withMessageColor(R.color.dialog_message)
                .withDialogColor(getResources().getColor(R.color.bg))
                .withIcon(R.drawable.app)
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(EffectsInfo.getInstance().getEffect())
                .withOk_ButtonText(ok)
                .withCancel_ButtonText(cancel)
                //.setCustomView(R.layout.custom_view, MainActivity.this)
                .setOk_BottonClick(new OkOnClickListener())
                .setCancel_BottonClick(new CancelOnClickListener())
                .show();
    }
    /**
     * 点击确认按钮执行的方法法
     * */
    private class OkOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            //比较，写入最高分数
            isBest(current_score);
            current_score = 0;
            tv_currenScore.setText(0 + "");
            Run_Function(GAME);
            NiftyDialogBuilder.getInstance(MainActivity.this).cancel();
        }
    }
    /**
     * 点击取消按钮执行的方法
     * */
    private class CancelOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            //比较，写入最高分数
            if(current_score>BestScode.getInstance(MainActivity.this).getBestScode()){
                BestScode.getInstance(MainActivity.this).setBestScode(current_score);
                tv_bestScore.setText(current_score+"");
            }
            finish();
            NiftyDialogBuilder.getInstance(MainActivity.this).cancel();
        }
    }
    /**
     *
     * 判断dialog执行确认按钮时的方法
     * */
    public void Run_Function(int n){
        switch (n){
            case NEW_GAME:
                //新游戏
                for(int x=0;x<arrays.length;x++){
                    for(int y=0;y<arrays[x].length;y++){
                        ViewCell view = lists.get(x*4+y);
                        view.setNumber(0);
                    }
                }
                random2Or4();
                break;
            case BACK_GAME:
                //继续游戏
                break;
        }
    }
}
