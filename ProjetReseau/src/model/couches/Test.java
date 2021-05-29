package model.couches;

public class Test {
    public static void main(String[] args) {
        System.out.print(fromAcssiToString("436f6e6e656374696f6e3a206b6565702d616c6976650d0a") + "Azul");
        System.out.print("bon");
        System.out.println("bon");
    }

    private static String fromAcssiToString(String ascii) {
        StringBuilder sb = new StringBuilder();
        int mod = 1;
        String word = "";
        for (String s : ascii.split("")) {
            word += s;
            if ((mod % 2) == 0) {
                sb.append((char) Integer.parseInt(word, 16));
                word = "";
                mod = 0;
            }
            mod++;
        }

        return sb.toString();
    }
}
