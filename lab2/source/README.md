# Lab 2
Najlepszy sprawdzony przeze mnie zestaw argumentów to:
```bash
-Xmx64m -Xms64m -XX:GCTimeRatio=4 +UseG1GC -XX:-ShrinkHeapInSteps
```

Aplikacja uruchomiona z tymi argumentami była czasem czyszczona przez GC.
Opcja `-ShrinkHeapInSteps` zapobiega częściowemu czyszczeniu sterty, dzięki czemu docelowa jej wielkość jest osiągana po pierwszym uruchomieniu GC.
Garbage collector G1GC jest najbardziej odpowiedni dla większych aplikacji, a co za tym idzie jest najbardziej agresywny.
Opcja GCTimeRation=4 określa, że garbage collector nie powinien działać dłużej niż 1/4 czasu działania programu.