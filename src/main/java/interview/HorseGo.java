package interview;

import java.util.ArrayList;
import java.util.List;

public class HorseGo {

    public static void main(String[] args) {
        List<int[]> result=new HorseGo().go(8,1,1);
        for(int[]arr:result){
            System.out.println(arr[0]+":"+arr[1]);
        }
    }

    final static int[][] directions=new int[][]{{-2,1},{2,1},{-2,-1},{2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};;

    final static int MARKED=-10;

    public List<int[]> go(int n,int x,int y){
        List<List<int[]>> deadRoutes=new ArrayList<List<int[]>>();
        int[][] arr=new int[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                arr[i][j]=4-(i==0||i==n?1:0)-(j==0||j==n?1:0);
            }
        }


        List<int[]> result=new ArrayList<int[]>();
        mark(n,x,y,arr,result);

        for(int index=1;index<n*n;index++){
            if(arr[x][y]==0){
                throw new IllegalArgumentException("no way found!");
            }

            int[] newDir=null;
            int sons=5;
            for(int[] direction:directions){
                int curX=x+direction[0];
                int curY=y+direction[1];
                int[] curDir=new int[]{curX,curY};
                if(curX<0 || curX>n-1 || curY<0||curY>n-1 || inDeadRoutes(result,curDir,deadRoutes)){
                    continue;
                }

                if(arr[curX][curY]!=MARKED && sons>arr[curX][curY]){
                    newDir=curDir;
                    sons=arr[curX][curY];
                }
            }
            if(newDir==null){
                deadRoutes.add(new ArrayList<int[]>(result));
            }else{
                x=newDir[0];
                y=newDir[1];
                mark(n, x, y, arr, result);
            }
        }
        return result;
    }

    private boolean inDeadRoutes(List<int[]> result,int[] pos,List<List<int[]>> deadRoutes){
        if(deadRoutes.isEmpty()){
            return false;
        }
        for(List<int[]> deadRoute:deadRoutes){
            if(deadRoute.size()!=result.size()+1){
                continue;
            }

            //如果末节点不同，直接continue
            int[]lastDead=deadRoute.get(deadRoute.size()-1);
            if(pos[0]!=lastDead[0] || pos[1]!=lastDead[1]){
                continue;
            }

            l1:for(int[] deadPoint:deadRoute){
                for(int[] r:result){
                    if(r[0]==deadPoint[0] && r[1]==deadPoint[1]){
                        continue l1;//如果有一个相等，则查下一个死节点
                    }
                }
                continue ;
            }
            return true;
        }

        return false;
    }

    private void mark(int n, int x, int y,  int[][] arr, List<int[]> result) {
        result.add(new int[]{x,y});
        arr[x][y]=MARKED;
        for(int[] direction:directions){
            int curX=x+direction[0];
            int curY=y+direction[1];
            if(curX<0 || curX>n-1 || curY<0||curY>n-1 || arr[curX][curY]==MARKED){
                continue;
            }

            arr[curX][curY]-=1;
            if(arr[curX][curY]<0){
                arr[curX][curY]=0;
            }
        }
    }
}
