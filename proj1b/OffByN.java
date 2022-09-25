public class OffByN implements CharacterComparator{

    private int Num;

    public OffByN(int N){
        Num = N;
    }
    @Override
    public boolean equalChars(char x, char y){
        int diff = Math.abs(x - y);
        return diff == Num;
    }
}