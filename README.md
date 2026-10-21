# Teploty ČR – Android widget

Android aplikace s widgetem, který zobrazuje pouze barevnou mapu teplot AMUT.
Klepnutí na widget otevře úplnou stránku AMUT.

## Automatické sestavení APK přes GitHub Actions

1. Vytvoř nový GitHub repository, například `TeplotyCR`.
2. Nahraj do něj celý obsah této složky (složky `app`, `.github` a soubory `build.gradle`, `settings.gradle`, `gradle.properties`).
3. Otevři kartu **Actions** a povol GitHub Actions, pokud se na to GitHub zeptá.
4. Spusť workflow **Build Android APK** tlačítkem **Run workflow**, nebo proveď push do větve `main`.
5. Po dokončení otevři běh workflow a v části **Artifacts** stáhni `TeplotyCR-debug-apk`.
6. Rozbal ZIP stažený z Artifacts. Soubor `app-debug.apk` nainstaluj do Androidu. Telefon může požadovat povolení instalace z daného zdroje.

## Přidání widgetu na Samsung

Po instalaci podrž prst na prázdném místě plochy → **Widgety** → **Teploty ČR** → přidej widget.

## Aktualizace

Widget načítá mapový obrázek přímo z:
`https://meteo.amut.net/images/pocasi-cr-teplota.png`

Android může kvůli úspoře baterie odložit aktualizaci. Periodická aktualizace je nastavena na přibližně 30 minut (Android WorkManager nepodporuje spolehlivé opakování po 5 minutách na pozadí). Při přidání widgetu se zároveň naplánuje jednorázové načtení.

## Poznámky

- Widget vyžaduje internetové připojení.
- Jde o debug APK určené pro vlastní instalaci; pro veřejné publikování by se měl vytvořit release build a podepsat vlastním klíčem.
- Zdrojová stránka po klepnutí: `https://meteo.amut.net/pocasi-cr-teplota`
