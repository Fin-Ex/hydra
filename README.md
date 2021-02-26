# Полезно:
`<.html><.body>
<.center>
<.table cellspacing=-452 cellpadding=-526>
    <.tr>
        <.td align=center>
        <.td align=left>
        <.td align=right>
            <.img src="Rolo_Frame.frame6" width=300 height=1070>
        <./td>
    <./tr>
<./table>
<./center>
<./body><./html>`

# Документация по коду / Source documentation:
###### Эффекты / Effects
Создавая класс эффектов в любом package, необходимо навесить на него аннотацию @Effect("EffectName") внутри которой, указывается имя эффекта (которое используется в .xml скилов).
- Например: `<effect name="EffectName" ...>`

# Update's links

1. [UPD 1.0](https://github.com/finfan222/L2JFinExGithub#upd-10)
2. [UPD 2.0](https://github.com/finfan222/L2JFinExGithub#upd-20)
3. [UPD 3.0](https://github.com/finfan222/L2JFinExGithub#upd-30)
4. [UPD 4.0](https://github.com/finfan222/L2JFinExGithub#upd-40)
4. [UPD 5.0](https://github.com/finfan222/L2JFinExGithub#upd-50)

# UPD 1.0

## Сервер
- Постепенный переезд на json
- lombok use
- Чистка ненужных конфигов
- Typo
- Изменения в .xml SA
- Изменения в .xml Skills
- Добавление Bleed/Poison типов эффектов, которые наносят урон в зависимости от сопротивления цели к этим статусам. (Пока не используется н ов будущем, все DamOverTime переедут на этот вариант если DamOverTime является Bleed/Poison типом)
- Рефакторинг
- И много всякого дерьма, которое обычно тут пишут

## Механика
Переработка всех SA в Interlude. Главная цель - сделать SA используемыми по максимуму, т.к. в мои времена Guidance, Evasion брали только не далёкие либо те, у кого не было других SA кристаллов кроме тех, что дают это дерьмо на пушку. **Все SA теперь имеют 1 LEVEL и определяются / накладываются динамически (относится к блидам и похожим эффектам, чтобы не прерывать левел-нить между лоу лвл чаром и хай лвл чаром.)

*Сперва указывается пунктом - SA, подпунктами которого - идут OLD и NEW именно в таком порядке.*

### SA

- Anger
  Increase P.Atk by N and decrease Max HP by 15%.
  Increase P.Atk by 15% and decrease Max HP by 15%.
- Acumen
  Increase casting SPD by 15%
  Increase casting speed by 15% for 1H blunts and for 7.5% for 2H blunts (if has it).
- Back Blow
  Increase critical rate for N while attacking from behind
  Increase critical rate for 172% while attacking from behind
- Cheap Shot
  With N% chance can reduce MP consume to 1 wile attacking from Bow weapon.
  Every Bow attack consumes only 1 MP.
- Crt. Anger
  Adds to P.Atk N value when attack was critical. Absorbs N HP.
  Absorb [LVL / 12 ^ 2] HP and adds 223 + [LVL / 3 ^ 2] P.Atk when attack is critical.
- Crt. Bleed
  With N% can apply bleed on enemy if attack was critical.
  With 70% (100% for blunts) applies bleed on target with power = 10% from P.Atk if attack was critical. Greetings to destroyers and archers! This bleed stacks with another bleed attacks.
- Crt. Drain
  Drain's N HP when attack was critical.
  Drains 40% from critical dealed damage and restores it to HP.
- Crt. Poison
  With N% chance can apply poison on enemy.
  With 70% chance can apply poiosn on target if attack was critical. Power = 66 + 10% M.Atk. This poison stacks with another poisons.
- Crt. Stun
  With N% chance can apply Stun status on enmy if attack was critical.
  With N% chance can apply Stun status on enmy if attack was critical. Also applying Stun skill has a 100% chance of success.
- Empower
  With N% chance can increase Magic power by N.
  Increase M.Atk by [caster LVL * 2.25]
- Evasion
  Increase Evasion by N.
  Increase Evasion by [LVL / 15 ^ 2]
- Focus
  Increase critical rate by N
  Increase critical rate by [56 + (LVL / 8 ^ 2)]
- Guidance
  Increase Accuracy by N
  Increase Accuracy by [LVL / 15 ^ 2]
- Haste
  Increase attack speed by 7%
  Increase attack speed by 15%
- Light
  Decrease weight of weapon by half
  Decrease weight of weapon by full (weapon weight = 0)
- Towering Blow
  Increase attack range by N
  Increase polearm attack range by 54 (and with hallberd stack full attack range will be 120)
- Magic Damage
  Give's 30% chance to deal additional magic damage +8 to M.Atk. (Used by Mother Tree Branch only DB info 146%)
  Increase all magical damage done by 12% (works only for directional spells which can deal dmg)
- Magic debuffs
  - `Paralyze`
    With N% chance when you casting a bad spell, target can be paralyzed.
    With 8% chance when you casting a bad spell, target can be paralyzed by Anchor spell which Magic Level is equals to your current level.
  - `Chaos`
    With N% chance when you casting a bad spell, target can be debuffed by Curse Chaos.
    With 33% chance when you casting a bad spell, target can be debuffed by Curse Chaos spell which Magic Level is equals to your current level.
  - `Weakness`
    With N% chance when you casting a bad spell, target can be debuffed by Curse Weakness.
    With 33% chance when you casting a bad spell, target can be debuffed by Curse Weakness by Anchor spell which Magic Level is equals to your current level.
  - `Hold`
    With N% chance when you casting a bad spell, target can be debuffed by Dryad Root.
    With 13% chance when you casting a bad spell, target can be debuffed by Dryad Root spell which Magic Level is equals to your current level.
  - `Poison`
    With N% chance when you casting a bad spell, target can be poisoned.
    With 20% chance when you casting a bad spell, target can be poisoned which Magic Level is equals to your current level.
  - `Magic Power`
    Increase magic When using magic, increases MP Consumption by 15%, and M. Atk. by N.
    Increase magic power of usable spell by randomly [100-150]%, increases all spells MP Consumption by 50%.
- Wide Blow
  Increase angle of attack for polearm to 120*
  Gives ability with full angle attack 360*
- Rsk. Haste
  Increase attack speed for N% when HP lower than 60%
  Increase attack speed by 1% each 1% of loosed HP when your HP is less than 60%.
- Rsk. Evasion
  Increase evasion by N when HP lower than 60%.
  Increase evasion by 1% for each 1% loosed HP when HP is lower than 60%.
- Rsk. Focus
  Increase critical rate by N when HP lower than 60%.
  Increase critical rate by 1% from max for each 1% loosed HP when HP lower than 60%.
- Miser
  Decrease SS consume when attacking from bow with N% chance
  All shots from bow consumes only 1 SS.
* * *
Пояснение по поводу Rsk. SA.:
Например Evasion.
Ваше HP = 3200
Текущее = 1239
% получаемого Evasion = +35,46875%. Т.е. отсчёт потери HP начинается с момента активации эффекта.
* * *

### Tatoo/Dye

*Татушки изменили своё направление и стремятся к новым горизонтам. Мне нравилось как работает система тату, но меня клинила "ограниченность" в её использовании. Т.е. какие-то тату мы можем ставить, а какие-то нет. Это как?*
Теперь тату делятся на 3 типа:
- Lesser Dye ![Lesser Dye](https://mmo-develop.ru/proxy.php?image=http%3A%2F%2Fl2j.ru%2Finterlude%2Fimg%2Ficons%2Fetc_con_hena_i00_0.png&hash=2a0fb709a970879ae71e736e4b14a7a5)
  - Standart +1 -3 (продаётся у НПЦ)
  - Graced +1 -2 (крафтится)
  - Greater +1 -1 (дроп с боссов)
- Medium Dye ![Medium Dye](https://mmo-develop.ru/proxy.php?image=http%3A%2F%2Fl2j.ru%2Finterlude%2Fimg%2Ficons%2Fetc_con_hena_i01_0.png&hash=7d785dab08f66eac6731bca197938c54)
  - Standart +2 -3 (продаётся у НПЦ)
  - Graced +2 -2 (крафтится)
  - Greater +2 -1 (дроп с боссов)
- Large Dye ![Large Dye](https://mmo-develop.ru/proxy.php?image=http%3A%2F%2Fl2j.ru%2Finterlude%2Fimg%2Ficons%2Fetc_con_hena_i02_0.png&hash=a0d3cd79e83a65487f3f183548a9e0fe)
  - Standart +3 -3 (продаётся у НПЦ)
  - Graced +3 -2 (крафтится)
  - Greater +3 -1 (дроп с боссов)

*Тату больше не имеют ограничителей и теперь любая профессия - может поставить на себя - любое тату.
Теперь все 3 ячейки открыты с самого начала, тут - кто ан что горазд.*

### CP Potions

Без этого не было бы смысла ничего делать вообще. банки ЦП - враг всех PvP режимов в открытом мире. Дворфы могли таскать эти банки тоннами и давать пиздюлей тем классам, которые не вытаскивали демагом - отхил цпшечки. Для оверов и варков это было совсем летально, т.к. они не отжирались от CP врага. Теперь, банки CP работаю так же как и обычные банки хила, но ПОКА ЧТО с базовыми статами, т.е.
- Lesser CP Potion восстанавливает 50 CP но за 5 секунд (10 CP в сек.)
- Greater CP Potion восстанавливает 200 CP тоже за 5 секунд (40 CP в сек.)

## PARAMS

Было проведено феншуйное обновление параметров, которые имели определённые не стыковки. например MEN дающий M.Def в дерьмовом поинте, а CON не дающий P.Def вообще никак. Возможно, это обуславливалось тем, что HP по факту = Защита, если сделать норм расчёт с калькулятором и вывести нужную цифру, но это же Ла2, какие в пизду цифры и расчёты?

- CON - `теперь дополнительно увеличивает и P.Def. Убраны пенальти для игроков которые уменьшали защиту в зависимости от эквипнутого шмота (по аналогии с маг. защитой в MEN). Нам - такие штучки не понадобятся. Изменения CON повлияют на физ. защиту. Она увеличится.`
- MEN
`Магическая защита больше не имеет штрафов при эквипе ювелирии, что позволяет получить чистую M.Def. (Раньше, часть магю защиты срезалась на ABS значения K (нормализующие константы), который нам - не понадобятся.)
Модификатор MENBonus увеличен с 1.010 до 1.016, что позволило получить необходимые показатели как в МП регене/макс МП, так и в даруемой от MEN - маг. защиты. Эти изменения повлияют на максимальные запасы МП, маг. защиту и регенерацию маны. Произойдёт их визуальное увеличение по отношению к базовой Lienage 2.`
- DEX
`Бонус показатель DEX увеличивается с 1.009 до 1.0167, позволяя получить базовый показатель бонуса равный 19% (вместо 10%)
Игрок получает возможность реально видеть как класс растёт с ростом характеристики DEX в "rogue" направление.
Данные изменения повлияют на все параметры и статы получаемые за счёт DEXBonus (lethal rate & shield block rate в них так же входят).`

## CRAFT

*Крафт - больная тема для любого олд фага, особенно, если вы - соло игрок. Качать крафтеров, спойлеров и сутки на пролёт ебашить ресы - не канон. Если вы захотите побачить, можете отправиться в Альбион Онлайн, игра подарит вам потрясающее ощущение от ежедневного говна вроде собери и купи продай. Но Lineage 2 - это о PvP, поэтому грех тратить время на сранный сбор ресурсов (по крайней мере в том кол-ве в котором предоставляла игра на 2006 год, тогда это было норм).*

Задачи для решения:
- Игрок увидев крафт - сразу нажимает exit ибо [А по щам?] оно ему надо? в сотый раз делать то же самое, что он делал уже когда-то.
- Дать кузнецам интересный задаток для начальной игры и будущего геймплея. Чтоб искры из жопы летели.
- Сделать родословную кузнеца особенной в социальном плане - их должен знать весь мир, если они лучшие из лучших.

Ресурсы снижаются до **SQRT[SQRT(CNT) * CNT]**.
Таким образом нам необходимо не *220 Iron Ore* а *57* и т.д. далее с другими ресами.
Большую роль - это сыграет на высоких уровнях, когда игроку нужно овер 10к ресов для крафта пушки с шансом 60-70% успеха.
Вообще упрощение крафта в плане задроства выведено из-за этого ньюанса.
Снижено кол-во необходимых ресурсов для создания изделий
Теперь кузнец имеет *Dwarven Craft 1 Level*. Других у него нет.

Всё ремесло разделилось на 4 типа:
1. Weapon Mastery
2. Armor Mastery
3. Jewel Mastery
4. Other Production

Каждый из спеков ремесла имеет свой уровень и он же сохраняется у игрока. Т.е. создавая Weapon игрок увеличивает опыт Weapon Mastery и уже поднимает уровень ковки именно в этой сфере. Иными словами, кузнецы разделяются на 4 типа, которые используют преимущества друг друга. Т.к. крафт был значительно упрощён в плане необходимого кол-ва ресурсов, данная фича не заставила себя долго ждать.

Спеки кузнеца имеют по 9 уровней, каждый из которых позволит овладеть технологией изготовки того или иного рецепта.
Кузнецы получили собственный рейтинг, который будет объявлятся для любого желающего на доске в любой кузнице (в городах).
Пока что, рейтинг ни на что не влияет и игроки будут просто любоваться собой, но дальше, кузнец станет индивидуумом, который будет иметь спрос на зказы конкретно от игроков, которые не хотят лопатить и гриндить на вторых окнах как отбитые дауны.

В роли гаранта будет выступать система Quote-Order, которая позволит игрокам оплачивать заказы, а кузнецам зарабатывать на стороне.
За каждый успешно скрафченный итем, игрок получает опыт в своём ремесле ( к которому относится созданное изделие).
**/wsminfo** команда позволит кузнецу посмотреть на текущий прогресс. Там же, ему будут поведаны все аспекты механики.

![](http://fin-ex.ru/github_tableofspec.png)

# UPD 2.0

- Формула для корректного расчёта полёта projectile скилов (wind strike, forse blaster и т.д.).
Что даёт: теперь у вас будет возможность получить урон от магии не через 0.4 сек как обычно, а через время завязанное на дистанции что логично ибо на дальние дистанции, скилы летят дольше чем в ближнем бою. А ещё, вы можете убить друг друга одновременно лол ;D пока вражеский projectile до вас летит и вы успели скастануть свой, раньше было невозможно из-за скорости каста и хиттайма скилов который не мог быть ниже 500 ms.
projectile skill переменная, для обозначения, что заклинание или скилл имеет flyTime.
- toggle reuse (если reuseDelay установлен у скилов Toggle типа, то их откат начинается после отмены toggle эффекта)
- alignment скилов указывается в xml скилов.
  - `(!) isMagic = EAlignment.MAGIC`
  - `(!) isAbility = EAlignment.ABILITY и т.д.`

- Stats больше не учитывает case стринги и имеет имя = Enum значению, Т.е. вы можете писать любым способом из представленных ниже:
  - `<mul order="0x30" stat="PATKSPD" val="1.05"/>`
  - `<mul order="0x30" stat="pAtKsPd" val="1.05"/>`

- Добавление политики для инвентаря, теперь можно манипулировать разрешениями на equip/unequip
- Удаление джула (старой отладки)
- Система физ. атак вынесена во внешний спектр
- система каста вынесена во внешний спектр
- енумы вынесены в овнешний спектр
- переход на логирование slf4j полностью детка
- arrowReloadSpd теперь является полноценным статом и добавляется фанком к игроку. Зависит от DEX.
- эффекты Poison & Bleed
  - (!) Согласно моим убеждениям, Bleed наносит урон **каждую секунду** с силой oldDamage / 3. Когда Poison наносит oldDamage **каждые 3 секунды**.
- Теперь, Resist Poison - снижает получаемый урон от ядов, а не шанс получить его
- Теперь, Resist Bleed - снижает получаемый урон от кровотоков, а не шанс получить его
- Хил скилы теперь действуют на нежить - нанося ей урон
  - На боссов, PERCENT хил действует в зависимости от максимального ХП кастера, а не босса.
- pAtkSpd и mAtkSpd теперь работают как делители и ускоряют перезарядку скилов по формуле делителя а не множителя на 0.N.
- Яды наложенные на мёртвых - теперь восстанавливают им ХП вместо нанесения урона
- Массовые способности изменились в механике
- Добавлен стат и обработка AoeDamage увеличивающий илии понижающий урон о тАоЕ скилов
- АоЕ скилы наносят больше доп. урона если жертвы находятся ближе к эпицентру
- От АоЕ скилов больше нельзя уклониться или отразить их, но по прежнему защитится щитом
- Переработка EffectResistance системы
  - Теперь есть 2 стата: Resist/Defense
    - Resist = снижает вероятность прохождения (Lion Heart)
    - Defense = снижает время действия эффекта
    - Ходовым является Defence, следующие способности были переработаны (включая и alignment скилов)
- Изменения в скилах:
![](http://fin-ex.ru/github_skillchanges.png)
![](http://fin-ex.ru/github_types.png)
- Система Parry (физ. атак и скилов)
  - Срабатывает только во фронте к цели (лицом)
  - Прибавляет к величине defence доп. значение зависящее от атаки вашего оружия которым было совершено парирование
  - У парного оуржия (кастетов и дуалов) вероятность парированяи увеличивается на 5% дополнительно
  - Парирование скилов является только если они projectile
  - Если в руках нет оружия то парирование не возможно
  - Мобы тоже умеют парировать атаки, если в руках - вейпон (указание в xml rightHand)
- Система мастерства персонажа (таланты)
  - Каждый N уровень, игрок получает очко LP (Lineage Point) уровни устанавливаются в конфигах
  - Action command вызова HTML мастерства - лежит в Alt+C
  - Добавить новые аталнты можно через talent_branches.json для нужной профы
  - Таланты открываются только со второй профы
  - При снижении уровней таланты никуда не деваются
  - Необходимо залить character_lineage.sql для корректного сохранения текущих атлантов
  - При взятии сабкласса, таланты скидываются и получается новый спек о ттекущей профы
  - У некоторых талантов есть ограничители в виде уровней или "надо изучить N талант для изучения данного"
  - Функция сброса всех талантов
    - Каждый раз сбрасывая таланты, стоимость следующего сброса увеличивается в 2 раза. Например:
    - 1 сброс = 5000 SP
    - 2 сброс = 10000 SP
    - 3 сброс = 20000 SP
    - 4 сброс = 40000 SP и т.д.
    - Максимальный сброс = Integer.MAX_VALUE
  - В конфигах вы можете настроить, что требует сброс - items, SP или бесплатно.
  - Стоимость сброса индивидуально у всех сабклассов чара.
- Первые mastery для Duelist на проверку и тест, по ходу дела будет дополняться
- Система комбо прокакчки для соло игроков (в пати не работает)
  - Каждый убитый моб даёт +1 к комбо
  - Каждая единица комбо увеличивает получаемый опыт о тследующего убийства на 1%
  - Кол-во комбинаций ограничено до Short.MAX_VALUE
- Квестовые реварды для следующих квестов выведены в дату, т.е. вы сможете менять награду или добавлять новую самолично (менять в json/quest_data.json). Пока пересмотрены следующие квесты и их награды
- Фикс багов прошлого*
- Система событий (не ещё не полный переход на неё)
- Первые тестовые таланты у профессии Gladiator
  - Parry
  - Triple Slasher
  - Disarm
  - Dual Wield
  - Confrontation
  - Dual Redirection
  - Heavy Grip
  - Dual Master
- Переработка механики Shield of Revenge: В течение 5 минут позволяет отражать урон от атак в ближнем бою projectile заклинание и возвращать его врагу.
   - Тормозит рыцаря на 1 секунду не позволяя делать какие либо действия.
   - Во время стопинга отражает спелл обратно.
   - Урон от отраженного спела наносится только спустя N времени как и в оффициальном касте скила flyTime.

### Client
- текстуры v1c01.utx
- удалить 2k19.utx из клиента
- модификация skillname.dat
- модификация skillsound.dat
- модификация skillgrp.dat
- Модификация interface.u

# UPD 3.0

### Mute статус

Теперь сало обрабатывается в коде иным образом и проверить под каким конкретно салом находится цель можно через isMuted(Alignment.TYPE);
Т.к. теперь существуют разные типы alignment скилов (Ultimate,Ability,Magic) вы как разрабы сами указываете какие сайленсы даёт тот или иной эффект.

### Дополнения к парированиям

Во время действия parry-stance эффекта (после праированяи атаки он включается на 3 секунды), наносимый урон с физ. атак увеличивается на 35 единиц. Этот урон чистый и идёт бонусом аля: "Атака спарирована и теперь можно использовать слабые места противника."

### Атака и защита от монстров

Базовые формулы по атаке-защите от монстров.
Прошлые формулы выпилены, т.к. они были говном по моему личному-авторитетному мнению.
Теперь, статы pAtkAngels увеличивают/уменьшают наносимый урон по ангелам.
Статы pDefAngels - СНИЖАЮТ или УВЕЛИЧИВАЮТ получаемый урон ОТ ангелов.
И аналогично со всеми расами.

### Регенерация

Обрабатывается отдельно от каждого ресурса.
HP/MP/CP имеют базовое время регена равно 3 сек.
- Новые статы:
  - HpRegenInterval
  - MpRegenInterval
  - CpRegenInterval

Снижают время перед регенерацией.

### Изменение механики Bladedancer

БД имеет очки называемые "Чувством ритма". С нанесением урона, эти очки набираются. Очки набираются до 100 (максимум) и дают +300% к базовой эффективности следующего танца.
- Когда игрок выходит из боевой стоки, очки обнуляются.
- После танца, очки обнуляются

![](http://fin-ex.ru/github_bd.png)

Формула: **[rhythm_feeling / 50 + 1]**

- Каждые 10 очков игрок оповещается по формуле сообщением → "Чувство ритма: +20%"
- На 70 очках → "Чувство ритма: +140%"

![](http://fin-ex.ru/github_bd2.png)

### Изменение механики Swordsinger
Механика оружия:
- Одноручный меч без щита - увеличивает вероятность критического удара на 100%.
- Дуалы - даёт возможность парировать атаки с шансом 10%
- Двуручник - сила критических ударов увеличивается на 100%

Дополнительные бонусы от брони:
- Light - увеличение уклонения +7%
- Heavy - вероятность получить критический удар -16%

Сила бафа меняется в зависимости от текущего оружия свордсингера.
- Sword {2min}
- Big Sword {-50% time, +15% to effect values}
- Dual Sword {time +100%}

![](http://fin-ex.ru/github_sws.png)

# UPD 4.0

## Client
* Модели QuestBoard
* Текустуры Quest Board
* SystemMessages

[Patch(System)](https://drive.google.com/file/d/1r0rG3bdZxdg8Np14XrYZmSFCoWJt6Hfm/view?usp=sharing)

## Server
* Фикс бага со слушателями oNEquip/unequip
* Добавлен метод removeTarget у Creature, теперь просто дёргаем его и целевой статус бсрасывается
* Дорабатываем NPC под рандомный квест генератор
* Фикс бага с талантом клада Dual Sword Mastery, не убирал потребление МП для скила Riposte Stance.
* Фикс бага с открытием листа талантов для гладиатора, ошибка из-за неверног оенум имени класса
* Заливка RND Quest Generator (beta)
* Добавлен Rnd.calcGuarantee - гарантированный рандом, который производит итерацию и возвращает элемент из списка/массива. Для примера:
У нас есть 5 вещей, каждую из вещей нужно проверить на случайный шанс дропа и при этом, последняя вещь всегда будет 100% выпадать. Калькуляция расчитывает рандом для каждой вещи исходя из 100 / кол-во вещей и получает шанс для первой вещи в 20%, 40% для второй 60% для третей и т.д.

### Random Quest Generator
* Игрок может взять квест который генерируется рандомно в каждом городе под каждый грейд
* Случайные квесты генерируемые на quest board применяют 4 типа:
1. Boss Hunt
2. Deliver
3. Killing Monsters
4. Resource Finder
Каждый из типов имеет собственную задачу, которую игрок - взявший квест должен реализовать.

* Каждый квест индивидуален и распространяется на одну игровую инстанцию, т.е. один квест может выполняться только одним игроком.
* При отменен квеста, игрок получает штраф 10-ти минутный на подбор новых квестов
* Квесты генерируются каждый день в 18:00 либо при запуске сервера
* TODO: функцию позволяющую выполнять квест несколькими игроками (shared)
* За каждый квест выдаётся собственная награда в зависимости от его типа.
* В каждом городе стоят quest board у которых можно взят ьэти квесты.

Например за Boss Hunt игрок получает Coin of Slayer в кол-ве 1 штука.

![](https://img-host.ru/dq6q.png)
![](https://img-host.ru/d7QN.png)
![](https://img-host.ru/gG80.png)

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
