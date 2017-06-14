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

    private final int numOfCeng = 3;
    private final int numOfYingCang = 13;
    private final int numOfOutput = 1;
    
    public BPNetImpl( int numOfInput, double rate, double mobp ){
        
        int[] layernum = { numOfInput, numOfYingCang, numOfOutput };

        this.mobp = mobp;
        this.rate = rate;

        layer               = new double[numOfCeng][];
        layerErr            = new double[numOfCeng][];
        layer_weight        = new double[numOfCeng][][];
        layer_weight_delta  = new double[numOfCeng][][];

        Random random = new Random();

        for( int i=0; i<numOfCeng; i++ ){
            layer[i]    = new double[layernum[i]];
            layerErr[i] = new double[layernum[i]];

            if( i<numOfCeng-1 ){
                layer_weight[i]         = new double[layernum[i]+1][layernum[i+1]];
                layer_weight_delta[i]   = new double[layernum[i]+1][layernum[i+1]];
                
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

        for( int i=0; i<layer.length-1; i++ ){
            for( int j=0; j<layer[i+1].length; j++){
                double z = layer_weight[i][ layer[i].length ][j];

                for( int k=0; k<layer[i].length; k++ ){
                    if( i==0 )
                        layer[i][k] = in[k];
                    z += layer_weight[i][k][j] * layer[i][k];
                } 

                layer[i+1][j] = transmitFunction( z ) ;
            }
        }

        // return output
        return layer[layer.length-1];
    }
    
    //逐层反向计算误差并修改权重
    @Override
    public void updateWeight( double tar ){
        int i = layer.length-1;

        layerErr[i][0] = rate * ( transmitFunction(tar) - layer[i][0] ) * layer[i][0] * ( 1-layer[i][0] ) ;

        for( ; i>0; i-- ){
            for( int j=0; j<layerErr[i-1].length; j++ ){
                
                double z = 0.0;
                for( int k=0; k<layerErr[i].length; k++ ){
                    if( z + i > 0 ) 
                        z = layer_weight[i-1][j][k] * layerErr[i][k];
                    else
                        z = 0;
                    
                    //隐含层动量调整
                    layer_weight_delta[i-1][j][k] = mobp * layer_weight_delta[i-1][j][k] 
                                                  + rate * layerErr[i][k] * layer[i-1][j];
                    
                    //隐含层权重调整
                    layer_weight[i-1][j][k] += layer_weight_delta[i-1][j][k];
                    
                    if( j==layerErr[i-1].length-1 ){
                        //截距动量调整
                        layer_weight_delta[i-1][j+1][k] = mobp * layer_weight_delta[i-1][j+1][k]
                                                        + rate * layerErr[i][k];
                        //截距权重调整
                        layer_weight[i-1][j+1][k] += layer_weight_delta[i-1][j+1][k];
                    }
                }
                //记录误差
                layerErr[i-1][j] = z * layer[i-1][j] * ( 1 - layer[i-1][j] );
            }
        }
    }

    @Override
    public void train( double[] in, double tar ){
        double[] out = computeOut( in );
        updateWeight( tar );
    }

    private double transmitFunction( double z ){
        double fenmu = 1 + Math.exp( -z * 0.01 );
        return 1.0 / fenmu;
    }
}

