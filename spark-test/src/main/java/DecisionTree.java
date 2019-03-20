import com.google.common.collect.Maps;

import java.util.*;

public class DecisionTree {

//    private static Map<String,List<String>> valueMap=new HashMap<>();

//    static {
//        valueMap.put("色泽",Arrays.asList("青绿","乌黑","浅白"));
//        valueMap.put("根蒂"  ,Arrays.asList("蜷缩","稍蜷","硬挺"));
//        valueMap.put("敲声",Arrays.asList("浊响","沉闷","清脆"));
//        valueMap.put("纹理"  ,Arrays.asList("清晰","稍糊","模糊"));
//        valueMap.put("脐部",Arrays.asList("凹陷","稍凹","平坦"));
//        valueMap.put("触感"  ,Arrays.asList("硬滑","软粘"));
//    }

    //,String parentType
    public DecisionTreeNode treeGenerate(List<Data> D,List<String> A,String attr,String attrV){
        Data firstD=D.get(0);
        Boolean typeDiff=false;
        for(Data d:D){
            if(!firstD.getY().equals(d.getY())){
                typeDiff=true;
            }
        }

        DecisionTreeNode node=new DecisionTreeNode();
        node.setCount(D.size());
        if(attr!=null){
            node.setAttr(attr);
            node.setAttrV(attrV);
        }
        if(!typeDiff){//如果d中样本全属于同一类别C
            node.setType(firstD.getY());
            return node;
        }

        Boolean attrEqual=true;
        Map<String,Integer> typeCntMap=new HashMap<>();
        Map<String,String> firstMap=getSubMap(firstD.getAttrs(),A);
        for(Data d:D){
            if(!firstMap.equals(getSubMap(d.getAttrs(),A))){
                attrEqual=false;
            }

            Integer cnt=typeCntMap.getOrDefault(d.getY(),0);
            typeCntMap.put(d.getY(),cnt+1);
        }

        //子节点中出现最多的类别
        String mostType=null;
        Integer mostTypeCnt=0;
        for(Map.Entry<String,Integer> typeCntEntry:typeCntMap.entrySet()){
            if(mostTypeCnt<typeCntEntry.getValue()){
                mostTypeCnt=typeCntEntry.getValue();
                mostType=typeCntEntry.getKey();
            }
        }
        if(attrEqual){//属性值都相同
            node.setType(mostType);
            return node;
        }

        //从A中选择最优划分属性ax
        String bestAttrName=getAttrByInformationScala(D,A);
        List<String> leftAttrs=new ArrayList<>(A);//剩余的属性集
        leftAttrs.remove(bestAttrName);

        Map<String,List<Data>> attrVDataMap=new HashMap<>();//该属性值-> data列表 map
        for(Data d:D){
            String attrValue=d.getAttrs().get(bestAttrName);
            List<Data> datas=attrVDataMap.get(attrValue);
            if(datas==null){
                datas=new ArrayList<Data>();
                attrVDataMap.put(attrValue,datas);
            }

            datas.add(d);
        }

        List<DecisionTreeNode> children=new ArrayList<>();
        node.setChildren(children);
        for(Map.Entry<String,List<Data>> entry:attrVDataMap.entrySet()){
            children.add(treeGenerate(entry.getValue(),leftAttrs,bestAttrName,entry.getKey()));
        }

        return node;
    }

    /**
     * 获得制定属性集合 对应的 值
     * @param valueMap 值map
     * @param attrs 属性集合
     * @return
     */
    private Map<String,String> getSubMap(Map<String,String> valueMap,List<String> attrs){
        Map<String,String> newMap=new HashMap<>();
        for(String a:attrs){
            newMap.put(a,valueMap.get(a));
        }
        return newMap;
    }

    /**
     * 根据信息熵增益原则 得出 最佳的属性名
     * @param D 数据集合
     * @param A 属性结合
     * @return
     */
    private String getAttrByInformationScala(List<Data> D,List<String> A){
        Map<String,Map<String,Map<String,Integer>>> gainDMap=new HashMap<>();
        //map 结构为x属性名->某个具体属性值->该属性取值下y的不同取值对应的数量
        for(Data d:D){//循环样本集
            for(Map.Entry<String,String> entry:d.getAttrs().entrySet()){//对于每一个属性
                if(!A.contains(entry.getKey())){
                    continue;
                }
                //该属性的取值-> map
                Map<String,Map<String,Integer>> map=gainDMap.get(entry.getKey());
                if(map==null){
                    map=new HashMap<String,Map<String,Integer>>();
                    gainDMap.put(entry.getKey(),map);
                }

                //该属性取值的 y值->属相 map
                Map<String,Integer> m=map.get(entry.getValue());
                if(m==null){
                    m=new HashMap<String,Integer>();
                    map.put(entry.getValue(),m);
                }
                m.put(d.getY(),m.getOrDefault(d.getY(),0)+1);
            }
        }

        Map<String,Double> gainMap=new HashMap<>();//不同属性名对应的gain值 map
        for(Map.Entry<String,Map<String,Map<String,Integer>>> entry:gainDMap.entrySet()){//attr 属性集
            Double entD=0d;
            for(Map.Entry<String,Map<String,Integer>> attrVEntry:entry.getValue().entrySet()){ //某个属性取值
                for(Integer i:attrVEntry.getValue().values()){
                    entD+=i*Math.log(D.size()/i.doubleValue())/Math.log(2)/D.size();
                }
            }
            gainMap.put(entry.getKey(),entD);
        }

        String bestAttr=null;
        Double minGain=0d;
        for(Map.Entry<String,Double> entry:gainMap.entrySet()){
            if(minGain==0d || minGain>entry.getValue()){
                bestAttr=entry.getKey();
                minGain=entry.getValue();
            }
        }
        return bestAttr;
    }

    public static void main(String[] args) {
//        List<Data> D=new ArrayList<>();
//
//        Data data=new Data();
//        data.setY("1");
//        Map<String,String> attrsV=Maps.newHashMap();
//        attrsV.put("a","1");
//        attrsV.put("b","2");
//        data.setAttrs(attrsV);
//        D.add(data);
//
//        data=new Data();
//        data.setY("1");
//        attrsV=Maps.newHashMap();
//        attrsV.put("a","1");
//        attrsV.put("b","1");
//        data.setAttrs(attrsV);
//        D.add(data);
//
//        data=new Data();
//        data.setY("2");
//        attrsV=Maps.newHashMap();
//        attrsV.put("a","2");
//        attrsV.put("b","1");
//        data.setAttrs(attrsV);
//        D.add(data);
//
//        data=new Data();
//        data.setY("2");
//        attrsV=Maps.newHashMap();
//        attrsV.put("a","2");
//        attrsV.put("b","2");
//        data.setAttrs(attrsV);
//        D.add(data);
//
//        data=new Data();
//        data.setY("2");
//        attrsV=Maps.newHashMap();
//        attrsV.put("a","2");
//        attrsV.put("b","2");
//        data.setAttrs(attrsV);
//        D.add(data);
//
//        data=new Data();
//        data.setY("1");
//        attrsV=Maps.newHashMap();
//        attrsV.put("a","2");
//        attrsV.put("b","2");
//        data.setAttrs(attrsV);
//        D.add(data);
//
//        List<String> attrs= Arrays.asList("a","b");
//        DecisionTreeNode node=new DecisionTree().treeGenerate(D,attrs,null,null);
//        System.out.println(node);

        List<Data> D=new ArrayList<>();

        D.add(createData("青绿","蜷缩","浊响","清晰","凹陷","硬滑","是"));
        D.add(createData("乌黑","蜷缩","沉闷","清晰","凹陷","硬滑","是"));
        D.add(createData("乌黑","蜷缩","浊响","清晰","凹陷","硬滑","是"));
        D.add(createData("青绿","蜷缩","沉闷","清晰","凹陷","硬滑","是"));
        D.add(createData("浅白","蜷缩","浊响","清晰","凹陷","硬滑","是"));
        D.add(createData("青绿","稍蜷","浊响","清晰","稍凹","软粘","是"));
        D.add(createData("乌黑","稍蜷","浊响","稍糊","稍凹","软粘","是"));
        D.add(createData("乌黑","稍蜷","浊响","清晰","稍凹","硬滑","是"));
        D.add(createData("乌黑","稍蜷","沉闷","稍糊","稍凹","硬滑","否"));
        D.add(createData("青绿","硬挺","清脆","清晰","平坦","软粘","否"));
        D.add(createData("浅白","硬挺","清脆","模糊","平坦","硬滑","否"));
        D.add(createData("浅白","蜷缩","浊响","模糊","平坦","软粘","否"));
        D.add(createData("青绿","稍蜷","浊响","稍糊","凹陷","硬滑","否"));
        D.add(createData("浅白","稍蜷","沉闷","稍糊","凹陷","硬滑","否"));
        D.add(createData("乌黑","稍蜷","浊响","清晰","稍凹","软粘","否"));
        D.add(createData("浅白","蜷缩","浊响","模糊","平坦","硬滑","否"));
        D.add(createData("青绿","蜷缩","沉闷","稍糊","稍凹","硬滑","否"));

        List<String> attrs= Arrays.asList("色泽",
                "根蒂",
                "敲声",
                "纹理",
                "脐部",
                "触感");
        DecisionTreeNode node=new PrePruningDecisionTree().treeGenerate(D,attrs,null,null);
        System.out.println(node);


    }

    private static Data createData(String color,String root,String sound,String edge,String mid,String touch,String isGoodMallon){
        Data data=new Data();
        data.setY(isGoodMallon);

        Map<String,String> attrsV=Maps.newHashMap();
        attrsV.put("色泽",color);
        attrsV.put("根蒂",root);
        attrsV.put("敲声",sound);
        attrsV.put("纹理",edge);
        attrsV.put("脐部",mid);
        attrsV.put("触感",touch);
        data.setAttrs(attrsV);
        return data;
    }
}

class DecisionTreeNode{
    private String attr;

    private String attrV;

    private String type;

    private Integer count;

    private List<DecisionTreeNode> children;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getAttrV() {
        return attrV;
    }

    public void setAttrV(String attrV) {
        this.attrV = attrV;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DecisionTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<DecisionTreeNode> children) {
        this.children = children;
    }
}
