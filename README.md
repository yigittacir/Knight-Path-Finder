# ♟️ Knight Path Finder (Satranç Atı Yol Bulma Simülasyonu)

Bu proje, bir satranç atının başlangıç noktasından hedefteki ödüle (Coin 🪙) ulaşırken kullandığı farklı yol bulma algoritmalarını görselleştiren bir Java uygulamasıdır. At, hedefe ulaşmaya çalışırken tahta üzerindeki engellere (Bombalar 💣) çarpmadan en uygun veya en hızlı yolu bulmaya çalışır.

## 🚀 Özellikler

Uygulama, aşağıdaki arama algoritmalarını kullanarak atın hareketlerini simüle eder ve performans karşılaştırması sunar:

* **Algoritmalar:**
    * A* (A-Star) Search
    * BFS (Breadth-First Search)
    * DFS (Depth-First Search)
    * Greedy Best-First Search
    * IDDFS (Iterative Deepening Depth-First Search)
* **Metrikler:** Her algoritma için gidilen adım sayısı, çalışma süresi (milisaniye ve nanosaniye cinsinden) hesaplanır.
* **Görselleştirme:** Algoritmaların çalışma mantığı ve çözüm yolu grafiksel arayüz üzerinde gösterilir.

## 🛠️ Kurulum ve Çalıştırma

Projeyi yerel bilgisayarınızda çalıştırmak için:

1.  Repoyu klonlayın:
    ```bash
    git clone [https://github.com/yigittacir/Knight-Path-Finder.git](https://github.com/yigittacir/Knight-Path-Finder.git)
    ```
2.  Proje dizinine gidin ve favori IDE'niz (IntelliJ, Eclipse, VS Code) ile açın.
3.  `Main` sınıfını çalıştırarak arayüzü başlatın.

## 👥 Geliştirici Ekibi ve Katkılar

Bu proje aşağıdaki ekip üyeleri tarafından geliştirilmiştir:

| Geliştirici | Görev / Katkı Alanı |
| :--- | :--- |
| **Yiğit Tacir** | A* Algoritması & UI (Arayüz) Geliştirme |
| **Cem Kağba** | Greedy Algoritması & UI (Arayüz) Geliştirme |
| **Selin Samray** | IDDFS Algoritması |
| **Semih Utku Canverdi** | BFS & DFS Algoritmaları |

---
*Proje, algoritmaların karşılaştırmalı analizi ve görselleştirilmesi amacıyla geliştirilmiştir.*
