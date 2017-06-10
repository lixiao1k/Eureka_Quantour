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
 *
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
                layer[i][j] = 1 / ( 1 + Math.exp(-0.01*z) );
            }
        }
        return layer[layer.length-1];
    }

    
    //逐层反向计算误差并修改权重
    @Override
    public void updateWeight( double tar[] ){
        int i=layer.length-1;
        for( int j=0; j<layerErr[i].length; j++ )
            layerErr[i][j] = layer[i][j] * (1-layer[i][j]) * ( 1/(1+Math.exp(-0.01*tar[j])) - layer[i][j] );

        while( i-->0 ){
            for( int j=0; j<layerErr[i].length; j++ ){
                double z = 0.0;
                for( int k=0; k<layerErr[i+1].length; k++ ){
                    z = z+i>0 ? layerErr[i+1][k]*layer_weight[i][j][k] : 0;
                    
                    //隐含层动量调整
                    layer_weight_delta[i][j][k] = mobp*layer_weight_delta[i][j][k]+rate*layerErr[i+1][k]*layer[i][j];
                    
                    //隐含层权重调整
                    layer_weight[i][j][k] += layer_weight_delta[i][j][k];
                    
                    if( j==layerErr[i].length-1 ){
                        //截距动量调整
                        layer_weight_delta[i][j+1][k] = mobp*layer_weight_delta[i][j+1][k]+rate*layerErr[i+1][k];
                        //截距权重调整
                        layer_weight[i][j+1][k] += layer_weight_delta[i][j+1][k];
                    }
                }
                //记录误差
                layerErr[i][j]=z*layer[i][j]*(1-layer[i][j]);
            }
        }
    }

    @Override
    public void train( double[] in, double tar[] ){
        double[] out = computeOut( in );
        updateWeight( tar );
    }
}
