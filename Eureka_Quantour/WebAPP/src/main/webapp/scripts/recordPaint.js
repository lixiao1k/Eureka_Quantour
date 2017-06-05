/**
 * Created by huihantao on 2017/6/5.
 */
function recordPaint(Elem,Data){
    //测试
    // 1. 创建画布对象
    var context = Elem.getContext('2d');
    // 2. 获取画布的宽度和高度
    const WIDTH = Elem.width;
    const HEIGHT = Elem.height;
    // 3. 定义坐标轴相对画布的内边距
    var padding = 20;//初始化内边距
    var paddingLeft = 60;//至少大于绘制文字的宽度
    var paddingBottom = 30;//至少大于绘制文字的高度
    // 4. 定义绘制坐标轴的关键点的坐标值
    var axisY = {// y轴的起点坐标值
        x : paddingLeft,
        y : padding
    };
    var origin = {// 原点坐标值(x轴与y轴相交点)
        x : paddingLeft,
        y : HEIGHT - paddingBottom
    };
    var axisX = {
        x : WIDTH - padding,
        y : HEIGHT - paddingBottom
    };
    // 5. 绘制坐标轴
    context.beginPath();
    context.moveTo(axisY.x,axisY.y);
    context.lineTo(origin.x,origin.y);
    context.lineTo(axisX.x,axisX.y);
    context.stroke();
    // 6. 绘制坐标轴的箭头
    context.beginPath();
    context.moveTo(axisY.x-5,axisY.y+10);
    context.lineTo(axisY.x,axisY.y);
    context.lineTo(axisY.x+5,axisY.y+10);
    context.stroke();

    context.beginPath();
    context.moveTo(axisX.x-10,axisX.y-5);
    context.lineTo(axisX.x,axisX.y);
    context.lineTo(axisX.x-10,axisX.y+5);
    context.stroke();

    // 定义折点的x轴值
    var pointsX = [];

    // 7. 绘制坐标轴的刻度(x轴的月份和y轴的金额)
    // x轴的月份
    var month = {
        x : paddingLeft,
        y : HEIGHT - paddingBottom
    }
    // 设置字体
    context.font = "14px 微软雅黑";
    // 设置垂直对齐
    context.textBaseline = "top";
    for(var i=1;i<=12;i++){
        pointsX[pointsX.length] = month.x;
        // 绘制月份信息
        context.fillText(i+"月",month.x,month.y);
        // 改变每次绘制的x坐标轴的值
        month.x += (axisX.x - origin.x)/12;
    }

    // 绘制y轴的金额
    // 从众多的关键金额中,取到最高金额
    /*
     var datas = [];
     for(index in Data){
     datas[datas.length] = Data[index];
     }
     function sortNumber(a,b){
     return a - b;
     }
     var max = datas.sort(sortNumber)[datas.length-1];
     */
    var max = Math.max.apply(Math,Data);

    var moneyY = (origin.y - axisY.y)/(max/500+1);

    // 定义绘制的坐标值
    var money = {
        x : axisY.x - 5,
        y : axisY.y + moneyY,
        jin : max
    }
    // 设置水品对齐
    context.textAlign = "right";
    // 遍历"最高值/间隔"次
    for(var i=0;i<max/500;i++){
        // 绘制金额
        context.fillText(money.jin+"元",money.x,money.y);
        // y轴向下移动(增加)
        money.y += moneyY;
        // 金额每次减500
        money.jin -= 500;
    }

    /*
     绘制折线
     * 12个折点的x轴值，对应12个月文字的x轴值
     * 折点的y轴值等于原点的y轴值-折点到原点的距离
     * 折点到原点的距离 = (3000点的y到原点的y的长度)*当前金额/3000
     */
    context.beginPath();
    for(var i=0;i<Data.length;i++){
        // 获取折点的x和y值
        var pointY = origin.y - (origin.y - (axisY.y + moneyY))*Data[i]/max;
        var pointX = pointsX[i];
        // 绘制折线
        if(i == 0){
            context.textAlign = "left";
            //context.textBaseline = "bottom";
            context.moveTo(pointX,pointY);
        }else{
            context.textAlign = "center";
            context.textBaseline = "bottom";
            context.lineTo(pointX,pointY);
        }
        // 绘制折点的金额
        context.fillText(Data[i],pointX,pointY);
    }
    context.stroke();
    // 绘制12个折点的圆
    for(var i=0;i<Data.length;i++){
        // 获取折点的x和y值
        var pointY = origin.y - (origin.y - (axisY.y + moneyY))*Data[i]/max;
        var pointX = pointsX[i];
        // 绘制圆
        context.fillStyle = "red";
        context.beginPath();
        context.arc(pointX,pointY,3,0,Math.PI*2);
        context.fill();
    }

}