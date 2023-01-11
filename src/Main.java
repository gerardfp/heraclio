import java.util.*;
import java.util.stream.Collectors;


class Card {

    enum Suit {
        HEARTS, SPADES, DIAMONDS, CLUBS;

        Color color(){
            return colors.get(this);
        }
    }

    enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING,
    }

    enum Color {
        BLACK, RED;

        @Override
        public String toString() {
            return this == BLACK ? "30" : "31";
        }
    }

    Suit suit;
    Rank rank;
    boolean back = false;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    Card down(){
        back = true;
        return this;
    }

    Card up(){
        back = false;
        return this;
    }

    Integer value(){
        return values.get(rank);
    }


    static EnumMap<Suit, EnumMap<Rank, String>> symbols = new EnumMap<>(Suit.class);
    static EnumMap<Rank, Integer> values = new EnumMap<>(Rank.class);
    static EnumMap<Suit, Color> colors = new EnumMap<>(Map.of(
            Suit.HEARTS, Color.RED,
            Suit.SPADES, Color.BLACK,
            Suit.DIAMONDS, Color.RED,
            Suit.CLUBS, Color.BLACK
    ));

    static {
        setSymbols(Suit.HEARTS, "🂱", "🂲", "🂳", "🂴", "🂵", "🂶", "🂷", "🂸", "🂹", "🂺", "🂻", "🂼", "🂽", "🂾");
        setSymbols(Suit.SPADES, "🂡", "🂢", "🂣", "🂤", "🂥", "🂦", "🂧", "🂨", "🂩", "🂪", "🂫", "🂬", "🂭", "🂮");
        setSymbols(Suit.DIAMONDS, "🃁", "🃂", "🃃", "🃄", "🃅", "🃆", "🃇", "🃈", "🃉", "🃊", "🃋", "🃌", "🃍", "🃎");
        setSymbols(Suit.CLUBS, "🃑", "🃒", "🃓", "🃔", "🃕", "🃖", "🃗", "🃘", "🃙", "🃚", "🃛", "🃜", "🃝", "🃞");

        setValues(1,2,3,4,5,6,7,8,9,10,11,12,13);
    }

    static void setSymbols(Suit suit, String... symbols){
        int i=0;
        Card.symbols.put(suit, new EnumMap<>(Rank.class));
        for(Rank rank: Rank.values()) {
            Card.symbols.get(suit).put(rank, symbols[i++]);
        }
    }

    static void setValues(Integer... values){
        int i=0;
        for(Rank rank: Rank.values()) {
            Card.values.put(rank, values[i++]);
        }
    }

    @Override
    public String toString() {
        if (back){
            return back();
        }
        return "\033[1;107;" + suit.color() +"m" + symbols.get(suit).get(rank) + "\033[0m";
    }

    static String back(){
        return "\033[1;107;34m🃏\033[0m";
    }

    static String empty(){
        return "\033[1;37m🂠\033[0m";
    }
}

class Deck extends ArrayList<Card> {
    Random random = new Random();

    Deck(){}

    Deck(List<Card> cards){
        super(cards);
    }

    Deck init(){
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank: Card.Rank.values()) {
                add(new Card(suit, rank));
            }
        }
        return this;
    }

    void add(Deck cards) {
        addAll(cards);
    }

    Card pop(){
        return remove(0);
    }

    Deck pop(int num){
        Deck sub = new Deck(subList(0, num));
        removeRange(0, num);
        return sub;
    }

    Card first(){
        return get(0);
    }

    Card last(){
        return get(size()-1);
    }

    Card random(){
        return remove(random.nextInt(size()));
    }

    void suffle(){
        Collections.shuffle(this);
    }

    Deck down(){
        forEach(Card::down);
        return this;
    }

    Deck up(){
        forEach(Card::up);
        return this;
    }

    @Override
    public String toString() {
        if (size() == 0) {
            return Card.empty();
        }
        return stream().map(Objects::toString).collect(Collectors.joining());
    }
}

class Tableau extends ArrayList<Deck> {
    Tableau(int size){
        for (int i = 0; i < size; i++) {
            add(new Deck());
        }
    }

    @Override
    public String toString() {
        return stream().map(Objects::toString).collect(Collectors.joining("\n"));
    }
}

public class Main {
    public static void main(String[] args) {
        Deck talon = new Deck();
        Deck waste = new Deck();

        Tableau foundations = new Tableau(4);
        Tableau depots = new Tableau(7);

        talon.init().down();
        talon.suffle();

        for (int i = 0; i < depots.size(); i++) {
            depots.get(i).add(talon.pop(i+1));
            depots.get(i).last().up();
        }

        System.out.println(foundations);
        System.out.println(talon);
        System.out.println(waste);
        System.out.println(depots);

    }
}



/*
    //String[] hearts = {"🂱", "🂲", "🂳", "🂴", "🂵", "🂶", "🂷", "🂸", "🂹", "🂺", "🂻", "🂼", "🂽", "🂾" };
    //String[] spades = {"🂡", "🂢", "🂣", "🂤", "🂥", "🂦", "🂧", "🂨", "🂩", "🂪", "🂫", "🂬", "🂭", "🂮" };
    //String[] diamonds = {"🃁", "🃂", "🃃", "🃄", "🃅", "🃆", "🃇", "🃈", "🃉", "🃊", "🃋", "🃌", "🃍", "🃎" };
    //String[] clubs = {"🃑", "🃒", "🃓", "🃔", "🃕", "🃖", "🃗", "🃘", "🃙", "🃚", "🃛", "🃜", "🃝", "🃞" };
 */