import java.util.HashMap;
import java.util.Map;

public class Boussole {
	Map<String,Integer> card = new HashMap<>();
	int dir;
	public Boussole() {
		card.put("Nord",90);
		card.put("Sud", 270);
		card.put("Est", 0);
		card.put("Ouest",180);
		card.put("Base", 0);
		int tmp = card.get("Base");
		card.put("BaseE",(tmp + 180)%360);
		dir = card.get("Nord");
	}
	
	public Boussole(int angle, int base) {
		super();
		card.put("Base", base);
		int tmp = card.get("Base");
		card.put("BaseE",(tmp + 180)%360);
		dir = angle;
	}
	
	public void setPos(int angle) {
		this.dir = (this.dir + angle)%360;
	}
	
	public int setPos(String pos) {
		if(card.containsKey(pos)) {
			int tmp = dir;
			dir = card.get(pos);
			return tmp - dir;
		}else {
			throw new IllegalArgumentException("La cardinalité voulu n'éxiste pas");
		}
	}
}

