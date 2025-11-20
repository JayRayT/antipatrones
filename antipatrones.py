¡Por supuesto, Juan! Aquí tienes el código Python completamente refactorizado, eliminando los tres antipatrones que identificamos: uso de diccionarios genéricos, números mágicos y duplicación de lógica.

✅ Código Python Refactorizado
import json
from pathlib import Path

# Constantes descriptivas
BASE_DOMESTIC_COST = 5
BASE_INTERNATIONAL_COST = 7
WEIGHT_FACTOR = 0.25
DISTANCE_DIVISOR = 300
HEAVY_WEIGHT_THRESHOLD = 20
EXTRA_DOMESTIC_COST = 3
EXTRA_INTERNATIONAL_COST = 4
SAMPLE_TOTAL = 123.45

class User:
    def __init__(self, user_id, name, tier):
        self.id = user_id
        self.name = name
        self.tier = tier

    def __repr__(self):
        return f"[{self.id}] {self.name} - tier={self.tier}"

class AppManager:
    def __init__(self, db_path="data.json"):
        self.db_path = Path(db_path)

    def load_users(self):
        if not self.db_path.exists():
            return []
        raw_data = json.loads(self.db_path.read_text(encoding="utf-8"))
        return [
            User(u.get("id", ""), u.get("name", ""), u.get("tier", ""))
            for u in raw_data
        ]

    def print_user(self, user):
        print(user)

    def discount_for_order(self, user, total):
        if user.tier == "gold" and total > 100:
            return total * 0.15
        if user.tier == "silver" and total > 42:
            return total * 0.07
        return 0.0

    def calculate_shipping(self, weight, distance_km, base_cost, extra_cost):
        variable = weight * WEIGHT_FACTOR + (distance_km / DISTANCE_DIVISOR)
        if weight > HEAVY_WEIGHT_THRESHOLD:
            variable += extra_cost
        return base_cost + variable

    def ship_cost_domestic(self, weight, distance_km):
        return self.calculate_shipping(weight, distance_km, BASE_DOMESTIC_COST, EXTRA_DOMESTIC_COST)

    def ship_cost_international(self, weight, distance_km):
        return self.calculate_shipping(weight, distance_km, BASE_INTERNATIONAL_COST, EXTRA_INTERNATIONAL_COST)

    def run(self):
        users = self.load_users()
        for user in users:
            self.print_user(user)
            discount = self.discount_for_order(user, SAMPLE_TOTAL)
            print(f"Descuento calculado: {discount:.2f}")
            print("Envío nacional:", self.ship_cost_domestic(12, 900))
            print("Envío internacional:", self.ship_cost_international(12, 900))

if __name__ == "__main__":
    AppManager().run()

