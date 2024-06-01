public class Bank {
    int bank_id;
    Card[] card;
    public Bank(){
        bank_id = 0;
        card = new Card[20];
        for(int i = 0; i<card.length; i++){
            card[i] = new Card(11111100+i,10000*i);
        }
    }

    public boolean certify_pay(int card_id, int charge){
        for(int i = 0; i < card.length; i++){
            if(card[i].card_check(card_id) && card[i].cash_check(charge)){
                    return true;
            }
        }
        return false;
    }
    public boolean cancel_pay(int card_id){
        for(int i = 0; i < card.length; i++){
            if(card[i].card_check(card_id)){
                return true;
            }
        }
        return false;
    }
}
