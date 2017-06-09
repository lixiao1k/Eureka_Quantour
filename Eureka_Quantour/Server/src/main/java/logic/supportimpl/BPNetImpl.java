package logic.supportimpl;

import java.util.Random;

import logic.supportservice.BPNetInterface;

/*
 * BPnet类：这里想建立输入单元为8个，两层隐含层，每个隐含层为13个单元，输出层单元为1的神经网络。
 * 首先初始化输入层到隐含层，隐含层之间，以及隐含层到输出层的权重矩阵；
 * 其次利用权重矩阵和输入层分别计算出每个隐含层节点数据
 * 之后利用计算得出的输出层数据与真实值进行比较，并逐层调节权重；
 * 反复上述过程直至精度达到要求或是达到迭代次数的要求；
 * 这里设置迭代次数为5000次；
 * 利用的测试数据集为Data2012to2015
 * 
 * 下图为训练之后的模型对Data2012to2015自身进行拟合的效果：
 * (这里由于自变量大概是10左右的数据，所以在利用激活函数1/(1+e^-ax))时，a取了0.01
 */
public class BPNetImpl implements BPNetInterface{
	//神经网络各层节点
    private double[][] layer;
    //神经网络各节点误差
    private double[][] layerErr;
    //各层节点权重
    private double[][][] layer_weight;
    //各层节点权重动量
    private double[][][] layer_weight_delta;
    //动量系数
    private double mobp;
    //学习系数
    private double rate;
    
    public BPNetImpl( int[] layernum, double rate, double mobp ){
        this.mobp = mobp;
        this.rate = rate;

        layer = new double[layernum.length][];
        layerErr = new double[layernum.length][];
        layer_weight = new double[layernum.length][][];
        layer_weight_delta = new double[layernum.length][][];
        Random random = new Random();
        for( int i=0; i<layernum.length; i++ ){
            layer[i] = new double[layernum[i]];
            layerErr[i] = new double[layernum[i]];
            if( i<layernum.length-1 ){
                layer_weight[i] = new double[layernum[i]+1][layernum[i+1]];
                layer_weight_delta[i] = new double[layernum[i]+1][layernum[i+1]];
                
                //随机初始化权重
                for( int j=0; j<layernum[i]+1; j++ )
                    for( int k=0; k<layernum[i+1]; k++ )
                        layer_weight[i][j][k] = random.nextDouble();
            }   
        }
    }

    
    //逐层向前计算输出
    @Override
    public double[] computeOut( double[] in ){

        for( int i=1; i<layer.length; i++ ){
            for( int j=0; j<layer[i].length; j++){
                double z = layer_weight[i-1][layer[i-1].length][j];

                for( int k=0; k<layer[i-1].length; k++ ){
                    layer[i-1][k]=i==1 ? in[k] : layer[i-1][k];
                    z += layer_weight[i-1][k][j]*layer[i-1][k];
                }
            //    System.out.println(z+"####");
                
                layer[i][j]=1/(1+Math.exp(-0.01*z));
            //    System.out.println("&&**"+layer[l][j]);
            }
        }
      //System.out.println("&&^^**"+layer[layer.length-1][0]);
        return layer[layer.length-1];
    }

    
    //逐层反向计算误差并修改权重
    @Override
    public void updateWeight( double[] tar ){
        int l=layer.length-1;
        for( int j=0; j<layerErr[l].length; j++ )
            layerErr[l][j] = layer[l][j] * (1-layer[l][j]) * ( 1/(1+Math.exp(-0.01*tar[j])) - layer[l][j] );

        while( l-->0 ){
            for( int j=0; j<layerErr[l].length; j++ ){
                double z = 0.0;
                for( int i=0; i<layerErr[l+1].length; i++ ){
                    z = z+l>0 ? layerErr[l+1][i]*layer_weight[l][j][i] : 0;
                    
                    //隐含层动量调整
                    layer_weight_delta[l][j][i] = mobp*layer_weight_delta[l][j][i]+rate*layerErr[l+1][i]*layer[l][j];
                    
                    //隐含层权重调整
                    layer_weight[l][j][i] += layer_weight_delta[l][j][i];
                    
                    if( j==layerErr[l].length-1 ){
                        //截距动量调整
                        layer_weight_delta[l][j+1][i] = mobp*layer_weight_delta[l][j+1][i]+rate*layerErr[l+1][i];
                        //截距权重调整
                        layer_weight[l][j+1][i] += layer_weight_delta[l][j+1][i];
                    }
                }
                //记录误差
                layerErr[l][j]=z*layer[l][j]*(1-layer[l][j]);
            }
        }
    }

    @Override
    public void train( double[] in, double[] tar ){
        double[] out = computeOut( in );
        updateWeight( tar );
    }
}
