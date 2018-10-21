package apriori;

import javafx.util.Pair;

import java.util.*;

/**
 * 用HashMap<List<Integer>, Integer>表示商品种类
 * [List[1,2,3], 1]
 * 代表同时选择了 1,2,3 号商品的人有1个
 */
public class Aporiori {
    private double confidence;
    private double support;
    private int associationCount;
    private byte[][] commodity;
    //当前的事务集合
    private Map<List<Integer>, Integer> current = new HashMap<>();
    //所有规则的集合
    private Set<Pair<List<Integer>, List<Integer>>> associationSet = new HashSet<>();

    public Aporiori(byte[][] commodity, double confidence, double support) {
        this.commodity = commodity;
        this.confidence = confidence;
        this.support = support;

        //初始化current
        for (int i = 0; i < commodity[1].length; i++) {
            List<Integer> key = new ArrayList();
            key.add(i);
            current.put(key, 0);
        }

        scan();
    }

    public void calc() {
        while (current.size() > 1) {
            jionAndPruning();
            scan();
            if (current.size() != 0)
                associationSet.addAll(genAssociation(current));
        }

        System.out.println("associationSet = " + associationSet);
        System.out.println("total rule count: " + associationSet.size());
    }

    //连接和剪枝
    private void jionAndPruning() {
        List<ArrayList<Integer>> keys = new ArrayList(current.keySet());
        current.clear();

        //对所有key做组合
        for (int i = 0; i < keys.size() - 1; i++) {
            for (int j = 1; j < keys.size(); j++) {
                List<Integer> a = (List<Integer>) keys.get(i).clone();
                //差集
                a.removeAll(keys.get(j));

                if (a.size() == 1) {
                    //并集
                    a.addAll(keys.get(j));
                    a.sort(Integer::compareTo);
//                    System.out.println("a = " + a);
                    current.put(a, 0);
                }
            }
        }

//        System.out.println("jionAndPruning current = " + current);
    }

    //扫描计算支持度，去掉支持度小的项
    private void scan() {
        //用于记录要删除的项
        List<List<Integer>> toDel = new ArrayList<>();

        //遍历获得支持度
        for (List<Integer> key : current.keySet()) {
            for (byte[] good : commodity) {

//                System.out.println("key = " + key);
//                System.out.println("good = " + Arrays.toString(good));

                boolean satisfy = true;
                //判断商品是否都有
                for (int i : key) {
                    if (good[i] != 1) {
                        satisfy = false;
                        break;
                    }
                }

                //存在,支持数加一
                if (satisfy) {
                    int v = current.get(key);
                    current.put(key, ++v);
                }
            }

//            System.out.println("before scan current = " + current);

            //去除支持度小的
            if (current.get(key) < support * commodity.length) {
                toDel.add(key);
            }
        }

        for (List<Integer> k : toDel)
            current.remove(k);

        System.out.println("current = " + current);
    }

    //根据超集产生关联规则，返回所有满足关联关系的map
    private Set<Pair<List<Integer>, List<Integer>>> genAssociation(Map<List<Integer>, Integer> superSetMap) {
        //子集满足支持度的map
        Map<List<Integer>, Integer> subSetMap = new HashMap<>();
        //规则的集合
        Set<Pair<List<Integer>, List<Integer>>> rule = new HashSet<>();

        //求出每一个超集的元素的所有子集中满足支持度的map
        for (List<Integer> key : superSetMap.keySet()) {
            //获得子集，然后计算置信度
            Map<List<Integer>, Integer> result = calcSupport(subSet(key), superSetMap.get(key));
            //总和所有的map
            subSetMap.putAll(result);

            for (List<Integer> k : result.keySet()) {
                List<Integer> temp = new ArrayList<>(key);
                k.sort(Integer::compareTo);
                temp.sort(Integer::compareTo);

                //输出所有规则
                temp.removeAll(k);
                System.out.println(k + " => " + temp);

                //总和所有规则
                rule.add(new Pair<List<Integer>, List<Integer>>(k, temp));
            }
        }

        //当前只有一个元素时停止
        System.out.println("superSetMap = " + superSetMap);
        if (superSetMap.keySet().iterator().next().size() == 1)
            return rule;

        //将所有超集中满足要求的子集作为新的超集递归计算
        if (subSetMap.size() > 0) {
            rule.addAll(genAssociation(subSetMap));
        }
        return rule;
    }

    //产生缺少一个元素的子集
    private List<List<Integer>> subSet(List<Integer> set) {
        List<List<Integer>> sub = new ArrayList<>();
        for (int i = 0; i < set.size(); i++) {
            List<Integer> list = new ArrayList<>(set);
            list.remove(i);
            sub.add(list);
        }
        return sub;
    }

    //计算子集的支持度计数，返回满足置信度的子集
    private Map<List<Integer>, Integer> calcSupport(List<List<Integer>> subSet, int setSupport) {
        Map<List<Integer>, Integer> resultSet = new HashMap<>();

        //对一个子集计算支持度
        for (List<Integer> list : subSet) {

            int support = 0;

            for (byte[] good : commodity) {

                boolean satisfy = true;
                //判断商品是否都有
                for (int i : list) {
                    if (good[i] != 1) {
                        satisfy = false;
                        break;
                    }
                }

                //存在,支持数加一
                if (satisfy) {
                    support++;
                }
            }

            //满足置信度
            if (support * confidence <= setSupport) {
                resultSet.put(list, support);
            }
        }

        return resultSet;
    }
}
