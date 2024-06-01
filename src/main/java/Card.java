public class Card {
    int card_id;
    int cash;
    public Card(int Card_id, int Cash){
        card_id = Card_id;
        cash = Cash;
    }
    public boolean card_check(int Card_id){
        if(card_id == Card_id){
            return true;
        }else{
            return false;
        }
    }
    public boolean cash_check(int Cash){
        if(cash >= Cash){
            return true;
        }else{
            return false;
        }
    }
}
