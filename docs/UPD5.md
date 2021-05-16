# UPD 5.0

## Client
[Patch](https://drive.google.com/file/d/1r0rG3bdZxdg8Np14XrYZmSFCoWJt6Hfm/view?usp=sharing)

## Server
- Заливаем dist о которй я забыл xD
- Форматирование код (частичное)
- Fix бага с талантами у глада в ветке
- Установка базы данных теперь производится из папки sql. Просто клацаем на installer (предварительно отредактировав данные внутри installer.bat). Установка файлов идёт автоматом, поэтому не надо добавлять .sql файлы вручную
- Fix бага с skillRadius у shockstomp
- Добавлен дополнительный require для таланта (requiredSkill)

### GLADIATOR talents
![](https://s4.aconvert.com/convert/p3r68-cdx67/bfdha-puxk1.png) Cumulative Rage - The attacks used with Sonic Rage do more damage depending on the current number of focuses.

![](https://s4.aconvert.com/convert/p3r68-cdx67/b7so3-po0ci.png) Sonic Assault - Sonic Move ability not consumes Sonic Force anymore. Has 70% chance to stun all enemies arround you in 150 radius for next 3 sec.

![](https://s4.aconvert.com/convert/p3r68-cdx67/b6l6u-eq6up.png) Challenger - Challenge players to a Duel (action) and win it to increase your influence as a Gladiator. In order for you to earn points, you must activate the insolence mode (/challenge) before the call, now that the mode is activated, challenging your opponent to a duel will consume 15,000 adena from your pocket. A duel is impossible if there is no money. Gladiators taking first places - increases their probability of a critical hit with abilities by 100% of their base value. The receivable EXP and SP are increased by 100% during the hunt for monsters. In addition, the top 10 gladiators have a salary of 15,000 adena for every 100 monsters they kill during the hunt (only those monsters that the gladiators kill personally are counted). Each weekly, at 6 o´clock in the morning, the points at the first [Slaughter Ten] are reset.

![](https://s4.aconvert.com/convert/p3r68-cdx67/b23pc-x78e3.png) Professional Anger - Increase the time of ability: Warcry by x2 times.

![](https://s4.aconvert.com/convert/p3r68-cdx67/bts51-gtk3m.png) Recoiled Blast - Sonic Blaster wave with 100% chance recoils from main target to another one which stays in raiuds 150 of a main target. After the bounce, the wave can be recoiled again, but with the less chance, and so on. A wave cannot recoil into the same target twice.

![](https://s4.aconvert.com/convert/p3r68-cdx67/b9rxv-433s0.png) Mana Control - The less health, the less mana consumed. Mana consumption on an abilities: [Triple Sonic Slash, Double Sonic Slash, Sonic Buster, Sonic Storm] is decreases by 1.5% for every 117 units of lost HP. The maximum reduction limit cannot exceed 50% of the actual MP capacity consumption.
Sonic Absorb - Absorbs charges at the target and restores [absorbed charges * 178.0] self CP.

![](https://i.ibb.co/X38skQX/Screenshot-4.png)

### WARLORD talents start developing...
* Fix: баги с предъидущими талантами гладиатора
* Fix: баг с иконками (не хватаело 2-ух)
