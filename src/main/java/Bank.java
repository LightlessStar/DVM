public class Bank {
    int bank_id;
    Card[] card;

    public Bank() {
        bank_id = 0;
        card = new Card[20];
        for (int i = 0; i < card.length; i++) {
            card[i] = new Card(1000 + i, 100000 * i);
        }
    }

    public boolean certify_pay(int card_id, int charge) {
        System.out.println("in certify pay");
        System.out.println("card_id: " + card_id);
        System.out.println("charge: " + charge);
        for (int i = 0; i < card.length; i++) {

            if (card[i].card_check(card_id) && card[i].cash_check(charge)) {
                card[i].pay(charge);
                System.out.println("certify pay ok");
                return true;
            }
        }
        return false;
    }

    public boolean cancel_pay(int card_id, int charge) {
        for (int i = 0; i < card.length; i++) {
            if (card[i].card_check(card_id)) {
                card[i].pay(-charge);
                return true;
            }
        }
        return false;
    }
}
