package com.github.superzhc.db.transform;

/**
 * @author superz
 * @create 2021/8/5 9:55
 */
public class e8 extends Transform2SQL {
    @Override
    public String sql() {
        return "INSERT INTO QQ (qq,mobile) VALUES(?,?)";
    }

    @Override
    public Object[] params(String data) {
        String[] ss = data.split("---");
        return new Object[]{ss[0], ss[1]};
    }

    public static void main(String[] args) throws Exception {
        e8 e8=new e8();
        e8.dealLine("D:\\downloads\\baidu\\8e.txt");
    }
}
