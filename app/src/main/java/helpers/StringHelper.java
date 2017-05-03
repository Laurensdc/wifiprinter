package helpers;

/**
 * Created by uiz on 02/05/2017.
 */

public class StringHelper {
    public static String swapus(String s) {
        String u = "·129·";
        String U = "·154·";
        String ret = s;
        for(int j = 0; j < StringHelper.checkCount(s, "ü"); j++) {
            for (int i = 0; i < ret.length(); i++) {
                if (ret.charAt(i) == 'ü') {
                    ret = ret.substring(0, i) + u + ret.substring(i + 1, ret.length());

                }
            }
        }

        return ret;
    }


    public static String swapU(String s) {
        String u = "·129·";
        String U = "·154·";
        String ret = s;
        for(int j = 0; j < StringHelper.checkCount(s, "Ü"); j++) {
            for (int i = 0; i < ret.length(); i++) {
                if (ret.charAt(i) == 'Ü') {
                    ret = ret.substring(0, i) + U + ret.substring(i + 1, ret.length());

                }
            }
        }

        return ret;
    }


    public static int checkCount(String s, String checkString){
//        int newPadding=paddingLeft;

        int count = 0;
        int flag = 0;

        for(int i = 0; i < s.length() - checkString.length() + 1; i++) {
            if (s.charAt(i) == checkString.charAt(0)) {
                flag++;
                for (int j = 1; j < checkString.length(); j++) {
                    if (s.charAt(i + j) == checkString.charAt(j)){
                        flag++;
                    }
                }
            }
            if(flag == checkString.length()){
                count += 1;
            }
            flag=0;
        }

        return count;
    }

}
