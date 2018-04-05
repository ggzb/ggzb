package com.ilikezhibo.ggzb.avsdk.badwordfilter;
public class Test {

   public static void main(String args[]){//测试敏感词过滤
      String nickName="我草";
      if(null != nickName){
         if(nickName.indexOf("狗")!=-1 || nickName.indexOf("逼")!=-1){
            System.out.println("有敏感词");
         }
         FilteredResult res = WordFilterUtil.filterText(nickName, '*');
         if(res.getBadWords().length() > 0){
            System.out.println("有敏感词");
         }
      }
      String word="234米%^&*赚2狗34条#狗";
      System.out.println(word.indexOf("狗"));
      word = word.replaceAll("狗", "*");
      FilteredResult res=WordFilterUtil.filterText(word, '*');
      System.out.println(res.getLevel());//检测到的敏感词中最高优先级的值 0为最小
      System.out.println(res.getFilteredContent().toString());//过滤后的字符串
      System.out.println(res.getBadWords());//敏感词列表
      System.out.println(res.getBadWords().length());//敏感词列表长度
      System.out.println(res.getOriginalContent());//原始字符串
   }

}
