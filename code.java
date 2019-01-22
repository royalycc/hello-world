package temp;
import java.math.BigDecimal;
import java.util.*;
public class Mix {
		int[][] graph;        //假设graph的行是顶点X集合（其中的顶点简称X顶点），列是顶点Y集合（其中的顶点简称Y顶点）
	    boolean[] xUsed;      //在每次循环中每个X顶点是否访问过
	    boolean[] yUsed;      //在每次循环中每个Y顶点是否访问过
	    static int[] match;          //每个Y顶点匹配的X顶点，即第i个Y顶点匹配的是第match[i]个X顶点
	    int len;              //图的大小为len*len
	    int[] less;           //与顶标变化相关
	

	    int[] X; //每个X顶点的顶标
	    int[] Y; //每个Y顶点的顶标，初始化为0

	    private static final int INFINITE = 0x6fffffff;
	    public Mix(int[][] data)
	    {
	        this.graph = data;
	        len = data.length;
	        xUsed = new boolean[len];
	        yUsed = new boolean[len];
	        match = new int[len];
	        less = new int[len];

	        X = new int[len];
	        Y = new int[len];

	        for (int i = 0; i < len; i++)
	        {
	            match[i] = -1;
	        }

	        //初始化每个X顶点的顶标为与之相连的边中最大的权
	        for (int k = 0; k < len; k++)
	        {
	            X[k] = data[k][0];
	            for (int l = 0; l < len; l++) 
	            {
	                X[k] = X[k] >= data[k][l] ? X[k] : data[k][l];
	            }
	        }
	    }
	    void  km() 
	    {
	        //遍历每个X顶点
	    	
	    	
	        for (int i = 0; i < len; i++) 
	        {
	            for (int j = 0; j < len; j++) 
	            {
	                less[j] = INFINITE;
	            }

	            while (true) 
	            {   //寻找能与X顶点匹配的Y顶点，如果找不到就降低X顶点的顶标继续寻找
	                for (int j = 0; j < len; j++) 
	                {
	                    xUsed[j] = false;
	                    yUsed[j] = false;
	                }

	                if (hungaryDFS(i)) break;  //寻找到匹配的Y顶点，退出

	                //如果没有找到能够匹配的Y顶点，则降低X顶点的顶标，提升Y顶点的顶标，再次循环
	                int diff = INFINITE;        //diff是顶标变化的数值
	                for (int j = 0; j < len; j++) 
	                {
	                    if (!yUsed[j]) diff = diff <= less[j] ? diff : less[j];
	                }
	                //diff等于为了使该顶点X能够匹配到一个Y顶点，其X的顶标所需要降低的最小值

	                //更新顶标
	                for (int j = 0; j < len; j++) 
	                {
	                    if (xUsed[j]) X[j] -= diff;
	                    if (yUsed[j]) Y[j] += diff;
	                    else less[j] -= diff;
	                }
	            }
	        }

	        //匹配完成，可以输出结果
	        int res = 0;
	        for (int i = 0; i < len; i++) 
	        {
	        	
	            res += graph[match[i]][i];
	        }
	        
	    }
	    private boolean hungaryDFS(int i) 
	    {
	        //设置这个X顶点在此轮循环中被访问过
	        xUsed[i] = true;

	        //对于这个X顶点，遍历每个Y顶点
	        for (int j = 0; j < len; j++) 
	        {
	            if (yUsed[j]) continue;   //每轮循环中每个Y顶点只访问一次
	            int gap = X[i] + Y[j] - graph[i][j];      //KM算法的顶标变化公式

	            //只有X顶点的顶标加上Y顶点的顶标等于graph中它们之间的边的权时才能匹配成功
	            if (gap == 0) 
	            {
	                yUsed[j] = true;
	                if (match[j] == -1 || hungaryDFS(match[j])) 
	                {
	                    match[j] = i;
	                    return true;
	                }
	            } 
	            else 
	            {
	                less[j] = less[j] <= gap ? less[j] : gap;
	            }
	        }

	        return false;
	    }

	public static void main(String[] str)
	{
		Random rand=new Random();
		int m=50,n=13,k=4;    //设置子任务，虚拟机，服务器数量
		int []pm=new int[m];
		int []bm=new int[m];
		double[] rm=new double[m];
		for(int i=0;i<m;i++)  //生成m个子任务的发送功率pm和子任务大小
		{
			pm[i]=50;
			bm[i]=rand.nextInt(451)+50;
			rm[i]=bm[i]*2.5;
		}
		int [][]dis=new int[m][n]; //生成子任务和虚拟机的距离
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
				dis[i][j]=25;
		int [][]map=new int[n][k];  //虚拟机和服务器的映射关系
		map[0][0]=1;map[1][0]=1;map[2][1]=1;map[3][1]=1;map[4][1]=1;map[5][2]=1;map[6][2]=1;
		map[7][2]=1;map[8][2]=1;map[9][3]=1;map[10][3]=1;map[11][3]=1;map[12][3]=1;
		double fr=15;   //单个服务器拥有的资源
		double fl=1.3;   //本地拥有的CPU资源
		
		double[] f=new double[n]; //每个虚拟机所拥有的资源
		f[0]=7.5;f[1]=7.5;
		f[2]=5;f[3]=5;f[4]=5;
		f[5]=3.75;f[6]=3.75;f[7]=3.75;f[8]=3.75;
		f[9]=3.75;f[10]=3.75;f[11]=3.75;f[12]=3.75;
		double[] tl=new double[m];  //每个任务当地执行的时间
		for(int i=0;i<m;i++)
		{
			tl[i]=rm[i]/fl;
		}
		double[] el=new double[m];    //每个任务在本地执行产生的能耗
		for(int i=0;i<m;i++)
		{
			el[i]=Math.pow(10, -8)*fl*rm[i];
		}
		double w=5*Math.pow(10,6);   //设置服务器带宽
		double w0=Math.pow(10,-10);    //设置噪声功率 
		double [][]g=new double[m][n];

		double[][] cost1=new double[m][n];
		for(int i=0;i<m;i++)    //计算出m行n列的t1
			for(int j=0;j<n;j++)
			{
				cost1[i][j]=1000*8*bm[i]/(w*Math.log(1.0+pm[i]*Math.pow(dis[i][j], -4)/Math.pow(10, -10))/Math.log(2));
			}
		double[][] cost2=new double[m][n];   //计算出m行n列的t-exe
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				cost2[i][j]=rm[i]/f[j];
			}
		double[][] cost3=new double[m][n];
		for(int i=0;i<m;i++)            //计算出m行n列的执行总时间
			for(int j=0;j<n;j++)
			{
				cost3[i][j]=cost1[i][j]+cost2[i][j];
			}
		
		double[][] er=new double[m][n];
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				er[i][j]=pm[i]*cost1[i][j]/1000000;
			}
		double[][] utility=new double[m][n];
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				utility[i][j]=0.5*(tl[i]-cost3[i][j])/tl[i]+0.5*(el[i]-er[i][j])/el[i];
				BigDecimal b=new BigDecimal(utility[i][j]);
				utility[i][j]=b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		int[][] newutility=new int[m][m];
		for(int i=0;i<m;i++)
			for(int j=0;j<m;j++)
			{
				if(j<=12)
				{
					newutility[i][j]=(int) Math.round(10000*(utility[i][j]));
				}
				else
					newutility[i][j]=0;
			}
		new Mix(newutility).km();
		System.out.println("\n bm arrays:\n");
		for(int i=0;i<n;i++)  //打印出距离数组
		{
			System.out.print(bm[match[i]]+" ");
		}
		System.out.println("\n");
		System.out.println("\n cost3 arrays:\n");
		for(int i=0;i<n;i++)  //打印出距离数组
		{
			System.out.print(cost3[match[i]][i]+" ");
		}
		System.out.println("\n");
		
		
	}
}
