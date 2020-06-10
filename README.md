Aplikácia reprezentujúca hru Cuberoll (viď http://www.flashgamesnexus.com/flash-games/Cube-Roll.php) v 2D priestore. Môžete si ju spustiť na webe alebo som pripravil aj konzolovú verziu. Je potrebné, aby ste mali na localhoste spustenú databázu postgres s menom používateľa "postgres" a heslom "123456789", prípadne potom zmenit v src\main\resources application.resources a service.resources.

Pri webovej stačí vybuildovať triedu GameStudioServer a následne budete môcť ísť na následujúcich linkoch kde bude hra bežať. 
Hierarchia stránok -> 
http://localhost:8080/ - stránka so všeobecným info o mne a tak ďalej
http://localhost:8080/menu - menu s hrami (momentálne len jedna), ktorí si intuitívne viete spustiť.

Pre konzolové rozhranie vybuildujte najprv GameStudioServer a potom Springclient.
