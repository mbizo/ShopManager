# ShopManagerApp

Real-life Android shop management app (Products + Sales + Room DB).

## Features
- Add products with cost, selling price, initial stock
- View product list with live updates
- Record sales with multiple items, auto stock reduction
- Local persistence with Room
- ViewBinding + simple 2-tab UI

## Build on GitHub
This repo includes a GitHub Actions workflow that:
- Sets up JDK 17
- Uses Gradle 8.7
- Builds a Debug APK (`app/build/outputs/apk/debug/app-debug.apk`)
- Uploads it as an artifact named `app-debug`

> Note: The workflow first tries `./gradlew`. If the wrapper is missing,
> it falls back to using the system Gradle (installed by the action).

## Local build
If building locally, install Android Studio (Giraffe+), then click *Build > Make Project*.

## Next steps
- Add receipts (PDF/print)
- Add payment methods (cash, mobile money, card)
- Add basic reports (daily totals, low stock)
- Add login/roles if needed
