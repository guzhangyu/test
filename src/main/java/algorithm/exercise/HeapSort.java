package algorithm.exercise;

public class HeapSort {

    public static void main(String[] args) {
        int arr[]=new int[]{1,7,6,8,3,4,5,99};
        HeapSort heapSort=new HeapSort();
        heapSort.heapSort(arr);
        for(int i=0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }

    public void heapSort(int[] arr){
        int endIndex=arr.length-1,limit=(int)Math.pow(2,Math.floor(Math.log(arr.length)/Math.log(2)));//倒数第二层的最末尾序号
        //调整成大顶堆
        while(endIndex>0){
            while(endIndex>limit-2){
                if(arr[endIndex/2]<arr[endIndex]){//如果需要调整
                    adjustToEnd(arr,endIndex/2,arr.length);
                }
                endIndex--;
            }
            limit/=2;
        }

        endIndex=arr.length-1;
        while(endIndex>0){
            swap(arr,0,endIndex);
            adjustToEnd(arr, 0, endIndex);
            endIndex--;
        }
    }

    /**
     * 将某一个元素调整至底部
     * @param arr
     * @param startIndex
     * @param endIndex
     */
    private void adjustToEnd(int[] arr, int startIndex, int endIndex) {
        int first=arr[startIndex];
        //底下元素不断上移，直到找到比first小的元素
        while(2*startIndex+1<endIndex){
            int newIndex=(2*startIndex+2<endIndex && arr[startIndex*2+1]<arr[startIndex*2+2])?
                    startIndex*2+2:startIndex*2+1;//拿到较大的元素

            if(arr[newIndex]<=first){
                break;
            }

            arr[startIndex]=arr[newIndex];
            startIndex=newIndex;
        }
        arr[startIndex]=first;
    }

    private void swap(int[] arr, int b, int a) {
        int temp=arr[a];
        arr[a]=arr[b];
        arr[b]=temp;
    }


}
