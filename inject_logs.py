import requests   # La herramienta #1 del hacker para manipular HTTP
import time       # Para controlar el 'ritmo' del ataque
import random     # Para la evasión (IP spoofing, User-Agent rotativo)
import argparse   # Para crear herramientas de línea de comandos (CLI) profesionales

# --- CONFIGURACIÓN ---
# Credenciales para saltarnos la seguridad básica (Basic Auth)
AUTH = ('soc-demo', 'demo123!')

# --- CLASE DE INYECCIÓN (Tu "Botnet" virtual) ---
class ThreatInjector:
    def __init__(self, url, auth):
        self.url = url
        self.auth = auth
        
        # Diccionario de IPs para spoofing (falsificación)
        self.botnet_ips = [
            "102.10.20.40", "198.51.100.22", "203.0.113.5", "185.20.50.11", 
            "8.8.8.8", "1.1.1.1", "45.33.22.11"
        ]
        self.safe_countries = ["US", "CO", "ES", "FR", "DE"]
        self.risk_countries = ["RU", "CN", "KP", "IR"]

    def _shoot(self, payload):
        """
        El 'gatillo'. Envía el paquete HTTP al servidor.
        """
        try:
            # requests.post es sincrónico. Espera a que el servidor responda.
            response = requests.post(self.url, json=payload, auth=self.auth, timeout=1)
            
            # Feedback visual
            if response.status_code == 201:
                print(f"[+] Impacto confirmado: {payload['type']} -> {payload['ip']}")
            else:
                print(f"[-] Fallo ({response.status_code}): {response.text}")
                
        except Exception as e:
            print(f"[!] Error de red: {e}")

    def scenario_normal(self):
        """
        Modo SIGILO: AUMENTADO a 15 eventos.
        """
        print("\n--- 🟢 EJECUTANDO TRÁFICO ORGÁNICO (NORMAL - 15 EVENTOS) ---")
        
        # CAMBIO: Aumentado a 15 iteraciones
        for _ in range(15):
            payload = {
                "type": "LOGIN_SUCCESS",
                "source": "web-portal",
                "ip": random.choice(self.botnet_ips), 
                "country": random.choice(self.safe_countries),
                "severity": "LOW",
                "metadata": "auth_method=mfa"
            }
            self._shoot(payload)
            # Reduje un poco el tiempo de espera para que no sea eterno (0.5s)
            time.sleep(0.5) 

    def scenario_suspicious(self):
        """
        Modo RUIDO: AUMENTADO a 30 eventos.
        """
        print("\n--- 🟠 EJECUTANDO SONDEO SOSPECHOSO (SUSPICIOUS - 30 EVENTOS) ---")
        
        for _ in range(30):
            event_type = random.choice(["HTTP_ERROR", "PAGE_NOT_FOUND", "ACCESS_DENIED"])
            
            payload = {
                "type": event_type,
                "source": "firewall-logs",
                "ip": f"192.168.1.{random.randint(10, 200)}", 
                "country": random.choice(self.safe_countries),
                "severity": "MEDIUM",
                "metadata": "suspicious_payload=true"
            }
            self._shoot(payload)
            time.sleep(0.2) # Un poco más rápido

    def scenario_critical(self):
        """
        Modo ATAQUE TOTAL: AUMENTADO a 100 eventos (Ráfaga Masiva).
        """
        attacker_ip = "10.66.6.66" # IP Fija del atacante
        print(f"\n--- 🔴 INICIANDO ATAQUE MASIVO DE FUERZA BRUTA (CRITICAL - 100 EVENTOS) ---")
        print(f"--- 🎯 Target IP: {attacker_ip} ---")
        
        # CAMBIO: Aumentado a 100 iteraciones (¡Ametralladora!)
        for i in range(100):
            payload = {
                "type": "LOGIN_FAILED",  
                "source": "auth-service",
                "ip": attacker_ip,       
                "country": "RU",         
                "severity": "HIGH",
                "metadata": f"user=admin attempted_pass=12345{i}"
            }
            self._shoot(payload)
            # 20ms entre disparos. 100 eventos tomarán unos 2-3 segundos.
            time.sleep(0.02) 

# --- PUNTO DE ENTRADA (CLI) ---
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="ThreatBeacon Attack Simulator")
    
    parser.add_argument("--scenario", 
                        choices=["normal", "suspicious", "critical"], 
                        required=True,
                        help="Select attack profile")
    
    parser.add_argument("--url",
                             default="https://threatbeacon-backend-production.up.railway.app/api/events",
                             help="Target API Endpoint")

    args = parser.parse_args()
    
    print(f"🎯 TARGET LOCKED: {args.url}")
    print(f"💣 SCENARIO: {args.scenario.upper()}")
    
    injector = ThreatInjector(args.url, AUTH)

    if args.scenario == "normal":
        injector.scenario_normal()
    elif args.scenario == "suspicious":
        injector.scenario_suspicious()
    elif args.scenario == "critical":
        injector.scenario_critical()