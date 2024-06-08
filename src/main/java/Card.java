public class Card {
    int card_id;
    int cash;

    public Card(int Card_id, int Cash) {
        card_id = Card_id;
        cash = Cash;
    }

    public boolean card_check(int card_id) {
        if (this.card_id == card_id) {
            return true;
        } else {
            return false;
        }
    }

    public boolean cash_check(int charge) {
        if (cash >= charge) {
            return true;
        } else {
            return false;
        }
    }

    public void pay(int charge) {
        cash -= charge;
    }
}
