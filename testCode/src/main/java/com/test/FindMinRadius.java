package com.test;

/**
 * @Author zhangyugu
 * @Date 2020/10/10 7:09 上午
 * @Version 1.0
 */
public class FindMinRadius {

    public static void main(String[] args) {
        System.out.println(new FindMinRadius().findRadius(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    public int findRadius(int[] preHouses, int[] heaters) {
        HouseHeatersMerger houseHeatersMerger = new HouseHeatersMerger(preHouses, heaters).invoke();
        int[] heaterIndexs = houseHeatersMerger.getHeaterIndexs();
        int[] expandedHouses = houseHeatersMerger.getExpandedHouses();
        int expandedHouseIndex = houseHeatersMerger.getExpandedHouseIndex();

        int maxRadius = heaters[0] - expandedHouses[0];
        for(int i=0; i<heaters.length-1; i++) {
            int radius = findRadius(expandedHouses, heaterIndexs[i], heaterIndexs[i+1]);
            if(maxRadius < radius) {
                maxRadius = radius;
            }
        }

        System.out.print("expandedHouses:");
        printArr(expandedHouses);
        System.out.println("expandedHouseIndex:" + expandedHouseIndex);
        maxRadius = Math.max(maxRadius, expandedHouses[expandedHouseIndex] - heaters[heaters.length-1]);
        return maxRadius;
    }

    private void printArr(int[] arr){
        for(int a : arr) {
            System.out.print(" " + a);
        }
        System.out.println();
    }

    /**
     * 查找两个heaterIndex之间，需要提供的heater半径
     * @param houses 房间间距
     * @param startIndex 起始heater下标
     * @param endIndex 结束heater下标
     */
    private int findRadius(int[] houses, int startIndex, int endIndex) {
        int startPosition = houses[startIndex], endPosition = houses[endIndex];
        double midPosition = (startPosition + endPosition) / 2;

        int start = startIndex, end = endIndex;
        while(start < end) {
            int mid = (start + end) / 2;
            if(houses[mid] == midPosition) {
                return (int)Math.round(midPosition - startPosition);
            }else if(houses[mid] < midPosition) {
                if(houses[mid + 1] > midPosition) {
                    return Math.max(houses[mid] - houses[startIndex], houses[endIndex] - houses[mid+1]);
                }
                // end = mid - 1;
                start = mid + 1;
            }else if(houses[mid] > midPosition) {
                if(houses[mid - 1] < midPosition) {
                    return Math.max(houses[mid-1] - houses[startIndex], houses[endIndex] - houses[mid]);
                }
                // start = mid + 1;
                end = mid - 1;
            }
        }
        System.out.println(String.format("%d:%d", startIndex, endIndex));
        System.out.println(String.format("%d:%d", start, end));
        // not reachable
        throw new RuntimeException("not reachable!");
    }

    private class HouseHeatersMerger {
        private int[] preHouses;
        private int[] heaters;
        private int[] heaterIndexs;
        private int[] expandedHouses;
        private int expandedHouseIndex;

        public HouseHeatersMerger(int[] preHouses, int[] heaters) {
            this.preHouses = preHouses;
            this.heaters = heaters;
        }

        public int[] getHeaterIndexs() {
            return heaterIndexs;
        }

        public int[] getExpandedHouses() {
            return expandedHouses;
        }

        public int getExpandedHouseIndex() {
            return expandedHouseIndex;
        }

        public int[] merge(int[] a, int[] b) {
            int len = a.length + b.length;
            int[] c = new int[len];

            for(int i=0, aIndex=0, bIndex=0; i<len; i++){
                while(aIndex<a.length && a[aIndex]<=b[bIndex]) {
                    if(a[aIndex] == b[bIndex]) {
                        len--;
                        bIndex++;
                    }
                    c[i] = a[aIndex++];
                }

                while(bIndex<b.length && b[bIndex]<a[aIndex]) {
//                    if(a[aIndex] == b[bIndex]) {
//                        len--;
//                    }
                    c[i] = b[bIndex++];
                }
            }
            return c;
        }

        public HouseHeatersMerger invoke() {
            //热水器下标
            heaterIndexs = new int[heaters.length];
            //拓展的房子地址
            expandedHouses = new int[heaters.length + preHouses.length];
            int preHouseIndex = 0;
            expandedHouseIndex = 0;
            //拓展房子地址（包含热水器），并得到热水器的下标数组
            for(int i=0; i<heaters.length; i++) {
                //在heaters[i]前的房子地址
                if(preHouseIndex<preHouses.length) {
                    while(preHouseIndex<preHouses.length && preHouses[preHouseIndex]<heaters[i]){
                        expandedHouses[expandedHouseIndex++] = preHouses[preHouseIndex++];
                    }
                }else {
                    expandedHouseIndex ++;
                }

                heaterIndexs[i] = expandedHouseIndex;
                expandedHouses[expandedHouseIndex] = heaters[i];

                if(preHouseIndex < preHouses.length && preHouses[preHouseIndex] == heaters[i]) {
                    preHouseIndex++;
                }
            }

            System.out.print("expandedHouses:");
            printArr(expandedHouses);
            preHouseIndex--;
            //如果热水器后面还有房子地址
            if(preHouseIndex < preHouses.length-1 && expandedHouses[expandedHouseIndex] != preHouses[preHouseIndex]) {
                while(preHouseIndex < preHouses.length-1 && expandedHouseIndex < expandedHouses.length-1) {
                    expandedHouses[++expandedHouseIndex] = preHouses[++preHouseIndex];
                }
            }

            System.out.println("preHouseIndex:" + preHouseIndex);
            System.out.print("expandedHouses:");
            printArr(expandedHouses);

            return this;
        }
    }
}
