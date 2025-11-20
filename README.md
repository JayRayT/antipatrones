# antipatrones
Juan David Rayo Tejada - 20231020023
Jonnatam Camacho Camargo - 20231020204

Antipatrones java

1. Antipatrón: Data Clumps / Uso de Map<String, String> como entidad
Ejemplo con antipatrón:
Map<String, String> user = new HashMap<>();
user.put("id", "123");
user.put("name", "Juan");
user.put("tier", "gold");

System.out.println(user.get("name"));


Corrección con clase User:
public class User {
    private final String id;
    private final String name;
    private final String tier;

public User(String id, String name, String tier) {
        this.id = id;
        this.name = name;
        this.tier = tier;
    }

public String getId() { return id; }
    public String getName() { return name; }
    public String getTier() { return tier; }
}


User user = new User("123", "Juan", "gold");
System.out.println(user.getName());



2. Antipatrón: Magic Numbers
Ejemplo con antipatrón:
double cost = 6 + weight * 0.3 + (distance / 250);
if (weight > 15) cost += 2;


Corrección con constantes:
private static final double BASE_COST = 6.0;
private static final double WEIGHT_FACTOR = 0.3;
private static final double DISTANCE_DIVISOR = 250.0;
private static final double HEAVY_WEIGHT_THRESHOLD = 15.0;
private static final double EXTRA_COST = 2.0;

double cost = BASE_COST + weight * WEIGHT_FACTOR + (distance / DISTANCE_DIVISOR);
if (weight > HEAVY_WEIGHT_THRESHOLD) cost += EXTRA_COST;

 3. Antipatrón: Código duplicado
 Ejemplo con antipatrón:
public double shipCostDomestic(double weight, double distance) {
    double base = 6;
    double variable = weight * 0.3 + (distance / 250);
    if (weight > 15) variable += 2;
    return base + variable;
}

public double shipCostInternational(double weight, double distance) {
    double base = 8;
    double variable = weight * 0.3 + (distance / 250);
    if (weight > 15) variable += 3;
    return base + variable;
}


Corrección con método reutilizable:
public double calculateShipping(double weight, double distance, double base, double extra) {
    double variable = weight * WEIGHT_FACTOR + (distance / DISTANCE_DIVISOR);
    if (weight > HEAVY_WEIGHT_THRESHOLD) variable += extra;
    return base + variable;
}

public double shipCostDomestic(double weight, double distance) {
    return calculateShipping(weight, distance, BASE_COST, EXTRA_COST);
}

public double shipCostInternational(double weight, double distance) {
    return calculateShipping(weight, distance, 8.0, 3.0);
}


Antipatrones python

1. Antipatrón: Data Clumps / Uso de diccionarios genéricos para representar usuarios
Ejemplo con antipatrón:
user = {"id": "001", "name": "Juan", "tier": "gold"}
print(user.get("name"))


 Corrección con clase User:
class User:
    def __init__(self, user_id, name, tier):
        self.id = user_id
        self.name = name
        self.tier = tier
        def __repr__(self):
        return f"[{self.id}] {self.name} - tier={self.tier}"


user = User("001", "Juan", "gold")
print(user)


Y modifica load_users() para construir objetos User:
def load_users(self):
    if not self.db_path.exists():
        return []
    raw_data = json.loads(self.db_path.read_text(encoding="utf-8"))
    return [User(u.get("id", ""), u.get("name", ""), u.get("tier", "")) for u in raw_data]


 2. Antipatrón: Magic Numbers
Ejemplo con antipatrón:
if weight > 20:
    variable += 3


Corrección con constantes descriptivas:
BASE_DOMESTIC_COST = 5
BASE_INTERNATIONAL_COST = 7
WEIGHT_FACTOR = 0.25
DISTANCE_DIVISOR = 300
HEAVY_WEIGHT_THRESHOLD = 20
EXTRA_DOMESTIC_COST = 3
EXTRA_INTERNATIONAL_COST = 4
SAMPLE_TOTAL = 123.45


Y úsalo en los métodos:
def ship_cost_domestic(self, weight, distance_km):
    variable = weight * WEIGHT_FACTOR + (distance_km / DISTANCE_DIVISOR)
    if weight > HEAVY_WEIGHT_THRESHOLD:
        variable += EXTRA_DOMESTIC_COST
    return BASE_DOMESTIC_COST + variable



 3. Antipatrón: Duplicación de lógica en métodos similares
 Ejemplo con antipatrón:
def ship_cost_domestic(self, weight, distance_km):
    #logica repetida

def ship_cost_international(self, weight, distance_km):
    # misma lógica con pequeñas diferencias...


 Corrección con función reutilizable:
def calculate_shipping(self, weight, distance_km, base_cost, extra_cost):
    variable = weight * WEIGHT_FACTOR + (distance_km / DISTANCE_DIVISOR)
    if weight > HEAVY_WEIGHT_THRESHOLD:
        variable += extra_cost
    return base_cost + variable

def ship_cost_domestic(self, weight, distance_km):
    return self.calculate_shipping(weight, distance_km, BASE_DOMESTIC_COST, EXTRA_DOMESTIC_COST)

def ship_cost_international(self, weight, distance_km):
    return self.calculate_shipping(weight, distance_km, BASE_INTERNATIONAL_COST, EXTRA_INTERNATIONAL_COST)




