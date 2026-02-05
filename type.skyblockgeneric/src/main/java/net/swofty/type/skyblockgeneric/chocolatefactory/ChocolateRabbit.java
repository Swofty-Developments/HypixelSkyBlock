package net.swofty.type.skyblockgeneric.chocolatefactory;

/**
 * Represents all chocolate rabbits that can be collected in Hoppity's Collection.
 * Total: 513 rabbits across all rarities.
 */
public enum ChocolateRabbit {
    // ==================== COMMON RABBITS (223) ====================
    AARON("Aaron", Rarity.COMMON, null, null, null),
    ABLE("Able", Rarity.COMMON, null, null, null),
    ACKER("Acker", Rarity.COMMON, null, null, null),
    ALBUS("Albus", Rarity.COMMON, null, "Crimson Isle", null),
    ALFIE("Alfie", Rarity.COMMON, null, null, null),
    ALICE("Alice", Rarity.COMMON, null, null, null),
    ALMOND_AMARETTO("Almond Amaretto", Rarity.COMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    ALPHA("Alpha", Rarity.COMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    ANGUS("Angus", Rarity.COMMON, null, null, null),
    ANNABELLE("Annabelle", Rarity.COMMON, null, null, null),
    ARCHIE("Archie", Rarity.COMMON, null, null, null),
    ARNIE("Arnie", Rarity.COMMON, null, null, null),
    AUDI("Audi", Rarity.COMMON, null, null, null),
    AUGUSTUS("Augustus", Rarity.COMMON, null, null, null),
    BABY("Baby", Rarity.COMMON, null, null, null),
    BADGER("Badger", Rarity.COMMON, null, null, null),
    BAGEL("Bagel", Rarity.COMMON, null, null, null),
    BALDWIN("Baldwin", Rarity.COMMON, null, null, null),
    BALOO("Baloo", Rarity.COMMON, null, null, null),
    BARNEY("Barney", Rarity.COMMON, null, null, null),
    BARTHOLOMEW("Bartholomew", Rarity.COMMON, null, null, null),
    BASKET("Basket", Rarity.COMMON, null, null, null),
    BAXTER("Baxter", Rarity.COMMON, null, null, null),
    BAYOU("Bayou", Rarity.COMMON, null, "Backwater Bayou", "Find §a15 §7unique egg locations in the §2⏣ Backwater Bayou§7."),
    BEATRICE("Beatrice", Rarity.COMMON, null, null, null),
    BERTHA("Bertha", Rarity.COMMON, null, null, null),
    BIBSY("Bibsy", Rarity.COMMON, null, null, null),
    BILLY("Billy", Rarity.COMMON, null, null, null),
    BINDI("Bindi", Rarity.COMMON, null, null, null),
    BINKY("Binky", Rarity.COMMON, null, null, null),
    BLAKE("Blake", Rarity.COMMON, null, null, null),
    BOB("Bob", Rarity.COMMON, null, null, null),
    BRAMBLE("Bramble", Rarity.COMMON, null, null, null),
    BREEZE("Breeze", Rarity.COMMON, null, null, null),
    BRIAN("Brian", Rarity.COMMON, null, null, null),
    BRIE("Brie", Rarity.COMMON, null, null, null),
    BRONSON("Bronson", Rarity.COMMON, null, null, null),
    BROOKS("Brooks", Rarity.COMMON, null, null, null),
    BRUCE("Bruce", Rarity.COMMON, null, null, null),
    BRUNO("Bruno", Rarity.COMMON, null, null, null),
    BUD("Bud", Rarity.COMMON, null, null, null),
    BUGSTER("Bugster", Rarity.COMMON, null, null, null),
    BUGSY("Bugsy", Rarity.COMMON, null, null, null),
    BUTTERCREAM_BLOSSOM("Buttercream Blossom", Rarity.COMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    CADET("Cadet", Rarity.COMMON, null, null, null),
    CALLIE("Callie", Rarity.COMMON, null, null, null),
    CARROT("Carrot", Rarity.COMMON, null, null, "Kill a §cRabbit Sprite Carrot King§7."),
    CAVE("Cave", Rarity.COMMON, null, "Deep Caverns", "Find §a15 §7unique egg locations in the §b⏣ Deep Caverns§7."),
    CHASE("Chase", Rarity.COMMON, null, null, null),
    CHESTER("Chester", Rarity.COMMON, null, null, null),
    CHIP("Chip", Rarity.COMMON, null, null, null),
    CHOMPER("Chomper", Rarity.COMMON, null, null, null),
    CHOMPSKY("Chompsky", Rarity.COMMON, null, null, null),
    CLAUDE("Claude", Rarity.COMMON, null, null, null),
    COCOA_COMET("Cocoa Comet", Rarity.COMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    COLLIN("Collin", Rarity.COMMON, null, null, null),
    COPPER("Copper", Rarity.COMMON, null, null, null),
    COTTONTAIL("Cottontail", Rarity.COMMON, null, null, null),
    CRICKET("Cricket", Rarity.COMMON, null, null, null),
    CRUSH("Crush", Rarity.COMMON, null, null, "Find §a100 §7duplicate rabbits."),
    CUDDLES("Cuddles", Rarity.COMMON, null, null, null),
    CUPCAKE("Cupcake", Rarity.COMMON, null, null, null),
    DELBOY("Delboy", Rarity.COMMON, null, null, null),
    DELILAH("Delilah", Rarity.COMMON, null, null, null),
    DEMI("Demi", Rarity.COMMON, null, null, null),
    DIGGER("Digger", Rarity.COMMON, null, null, null),
    DUCHESS("Duchess", Rarity.COMMON, null, null, null),
    DULCE_DRIZZLE("Dulce Drizzle", Rarity.COMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    DUSTY("Dusty", Rarity.COMMON, null, null, null),
    ELLIE("Ellie", Rarity.COMMON, null, null, null),
    EMBER("Ember", Rarity.COMMON, null, "Crimson Isle", "Find §a15 §7unique egg locations in the §c⏣ Crimson Isle§7."),
    EMERSON("Emerson", Rarity.COMMON, null, null, null),
    EMMA("Emma", Rarity.COMMON, null, "Deep Caverns", null),
    ESPRESSO_ECLAIR("Espresso Eclair", Rarity.COMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    ETA("Eta", Rarity.COMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    FERGIE("Fergie", Rarity.COMMON, null, null, null),
    FIEVEL("Fievel", Rarity.COMMON, null, null, null),
    FLUFFY("Fluffy", Rarity.COMMON, null, null, null),
    FRANCINE("Francine", Rarity.COMMON, null, null, null),
    FRANK("Frank", Rarity.COMMON, null, null, null),
    FRANKIE("Frankie", Rarity.COMMON, null, null, null),
    FUDGE("Fudge", Rarity.COMMON, null, null, null),
    FUZZY("Fuzzy", Rarity.COMMON, null, null, null),
    GEORGE("George", Rarity.COMMON, null, null, null),
    GINGER("Ginger", Rarity.COMMON, null, null, null),
    GINNY("Ginny", Rarity.COMMON, null, null, null),
    GIZMO("Gizmo", Rarity.COMMON, null, null, null),
    GLORIA("Gloria", Rarity.COMMON, null, null, null),
    GOUDA("Gouda", Rarity.COMMON, null, null, null),
    GRACIE("Gracie", Rarity.COMMON, null, null, null),
    GUINNESS("Guinness", Rarity.COMMON, null, null, null),
    GUNTHER("Gunther", Rarity.COMMON, null, null, null),
    HADLEY("Hadley", Rarity.COMMON, null, null, null),
    HARLAND("Harland", Rarity.COMMON, null, "Spider's Den", null),
    HARLEY("Harley", Rarity.COMMON, null, null, null),
    HEFNER("Hefner", Rarity.COMMON, null, null, null),
    HEIDIE("Heidie", Rarity.COMMON, null, null, null),
    HERBIE("Herbie", Rarity.COMMON, null, null, null),
    HERSHEY("Hershey", Rarity.COMMON, null, null, null),
    HONDO("Hondo", Rarity.COMMON, null, null, null),
    HOPPER("Hopper", Rarity.COMMON, null, null, null),
    HUBBY("Hubby", Rarity.COMMON, null, "Hub", "Find §a15 §7unique egg locations in the §bHub§7."),
    HUCK("Huck", Rarity.COMMON, null, null, null),
    HUGO("Hugo", Rarity.COMMON, null, null, null),
    HUMPHREY("Humphrey", Rarity.COMMON, null, null, null),
    HUNTER("Hunter", Rarity.COMMON, null, null, null),
    IGGY("Iggy", Rarity.COMMON, null, null, null),
    INDIE("Indie", Rarity.COMMON, null, null, null),
    JACQUELINE("Jacqueline", Rarity.COMMON, null, "The Park", null),
    JADE("Jade", Rarity.COMMON, null, "Crystal Hollows", "Find §a15 §7unique egg locations in the §5⏣ Crystal Hollows§7."),
    JAKE("Jake", Rarity.COMMON, null, null, null),
    JAMES("James", Rarity.COMMON, null, null, null),
    JAMMER("Jammer", Rarity.COMMON, null, null, null),
    JASMINE("Jasmine", Rarity.COMMON, null, null, null),
    JAZMIN("Jazmin", Rarity.COMMON, null, null, null),
    JEFFERY("Jeffery", Rarity.COMMON, null, null, null),
    JERRY("Jerry", Rarity.COMMON, null, null, "Kill a §6Villager Sprite Golden Jerry§7."),
    JOEY("Joey", Rarity.COMMON, null, null, null),
    JONAH("Jonah", Rarity.COMMON, null, null, null),
    JOSEPHINE("Josephine", Rarity.COMMON, null, null, null),
    KRONK("Kronk", Rarity.COMMON, null, "Crystal Hollows", null),
    LAZY("Lazy", Rarity.COMMON, null, "Gold Mine", null),
    LENNY("Lenny", Rarity.COMMON, null, null, null),
    LILY("Lily", Rarity.COMMON, null, null, null),
    LONE_RANGER("Lone Ranger", Rarity.COMMON, null, null, null),
    LOTTE("Lotte", Rarity.COMMON, null, null, null),
    LOUIE("Louie", Rarity.COMMON, null, null, null),
    MANDY("Mandy", Rarity.COMMON, null, null, null),
    MARLOW("Marlow", Rarity.COMMON, null, null, null),
    MARSHALL("Marshall", Rarity.COMMON, null, "Galatea", null),
    MAUI("Maui", Rarity.COMMON, null, null, null),
    MAX("Max", Rarity.COMMON, null, null, null),
    MICKEY("Mickey", Rarity.COMMON, null, null, null),
    MILES("Miles", Rarity.COMMON, null, null, null),
    MILLY("Milly", Rarity.COMMON, null, null, null),
    MIMI("Mimi", Rarity.COMMON, null, "Dwarven Mines", "Find §a15 §7unique egg locations in the §2⏣ Dwarven Mines§7."),
    MITE("Mite", Rarity.COMMON, null, "The End", null),
    MOCHI("Mochi", Rarity.COMMON, null, null, null),
    MOLLY("Molly", Rarity.COMMON, null, null, null),
    MONA("Mona", Rarity.COMMON, null, null, null),
    MOODY("Moody", Rarity.COMMON, null, null, null),
    MOOKIE("Mookie", Rarity.COMMON, null, null, null),
    MOPSY("Mopsy", Rarity.COMMON, null, null, null),
    MORRIS("Morris", Rarity.COMMON, null, null, null),
    MORTY("Morty", Rarity.COMMON, null, "Dungeon Hub", "Find §a15 §7unique egg locations in the §c⏣ Dungeon Hub§7."),
    MOTFER("Motfer", Rarity.COMMON, null, "Dwarven Mines", null),
    NATALIE("Natalie", Rarity.COMMON, null, null, null),
    NED("Ned", Rarity.COMMON, null, null, null),
    NIBBLES("Nibbles", Rarity.COMMON, null, null, null),
    NIKO("Niko", Rarity.COMMON, null, null, null),
    NIZA("Niza", Rarity.COMMON, null, null, null),
    NU("Nu", Rarity.COMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    NUTMEG("Nutmeg", Rarity.COMMON, null, null, null),
    OLETTA("Oletta", Rarity.COMMON, null, null, null),
    OLIVER("Oliver", Rarity.COMMON, null, null, null),
    OLIVETTE("Olivette", Rarity.COMMON, null, null, null),
    OLIVIER("Olivier", Rarity.COMMON, null, null, null),
    OLLIE("Ollie", Rarity.COMMON, null, null, null),
    ONION("Onion", Rarity.COMMON, null, "Backwater Bayou", null),
    PADDY("Paddy", Rarity.COMMON, null, null, null),
    PATCH("Patch", Rarity.COMMON, null, null, null),
    PEBBLES("Pebbles", Rarity.COMMON, null, null, null),
    PENNY("Penny", Rarity.COMMON, null, null, null),
    PEONY("Peony", Rarity.COMMON, null, null, null),
    PETUNIA("Petunia", Rarity.COMMON, null, null, null),
    PICKLES("Pickles", Rarity.COMMON, null, null, null),
    PICKY("Picky", Rarity.COMMON, null, "Gold Mine", "Find §a15 §7unique egg locations in the §6⏣ Gold Mine§7."),
    PINKY("Pinky", Rarity.COMMON, null, null, null),
    POPPY("Poppy", Rarity.COMMON, null, null, null),
    PORTER("Porter", Rarity.COMMON, null, null, null),
    POTATO("Potato", Rarity.COMMON, null, "The Farming Islands", null),
    QUENTIN("Quentin", Rarity.COMMON, null, null, null),
    RAZZIE("Razzie", Rarity.COMMON, null, null, null),
    REGINALD("Reginald", Rarity.COMMON, null, null, null),
    REMI("Remi", Rarity.COMMON, null, null, null),
    RESSIE("Ressie", Rarity.COMMON, null, null, null),
    RICKY("Ricky", Rarity.COMMON, null, null, null),
    RILEY("Riley", Rarity.COMMON, null, null, null),
    ROGUE("Rogue", Rarity.COMMON, null, "Hub", null),
    ROLF("Rolf", Rarity.COMMON, null, null, null),
    ROSCO("Rosco", Rarity.COMMON, null, null, null),
    ROSS("Ross", Rarity.COMMON, null, null, null),
    ROWDY("Rowdy", Rarity.COMMON, null, null, null),
    RUBEN("Ruben", Rarity.COMMON, null, null, null),
    RUPERT("Rupert", Rarity.COMMON, null, null, null),
    RYDER("Ryder", Rarity.COMMON, null, null, null),
    SASSY("Sassy", Rarity.COMMON, null, null, null),
    SCOOTER("Scooter", Rarity.COMMON, null, null, null),
    SCOTCH("Scotch", Rarity.COMMON, null, null, null),
    SCOUT("Scout", Rarity.COMMON, null, null, null),
    SCUBA("Scuba", Rarity.COMMON, null, null, null),
    SELENE("Selene", Rarity.COMMON, null, null, null),
    SKIPPE("Skippe", Rarity.COMMON, null, null, null),
    SMOKEY("Smokey", Rarity.COMMON, null, null, null),
    SNIFFLES("Sniffles", Rarity.COMMON, null, null, null),
    SNOPPY("Snoppy", Rarity.COMMON, null, null, null),
    SNUFFY("Snuffy", Rarity.COMMON, null, null, null),
    SOON("Soon", Rarity.COMMON, null, "Dungeon Hub", null),
    SOPHIE("Sophie", Rarity.COMMON, null, null, null),
    SORBET("Sorbet", Rarity.COMMON, null, null, null),
    SPENCER("Spencer", Rarity.COMMON, null, null, null),
    SPOT("Spot", Rarity.COMMON, null, null, null),
    STANLEY("Stanley", Rarity.COMMON, null, null, null),
    STINKY("Stinky", Rarity.COMMON, null, "The Farming Islands", "Find §a15 §7unique egg locations in §eThe Farming Islands§7."),
    STRIKER("Striker", Rarity.COMMON, null, "Spider's Den", null),
    STUART("Stuart", Rarity.COMMON, null, null, null),
    SURI("Suri", Rarity.COMMON, null, null, null),
    TAGALONG("Tagalong", Rarity.COMMON, null, null, null),
    TAU("Tau", Rarity.COMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    TEDDY("Teddy", Rarity.COMMON, null, null, null),
    THALAI("Thalai", Rarity.COMMON, null, null, null),
    THEO("Theo", Rarity.COMMON, null, null, null),
    THEODORE("Theodore", Rarity.COMMON, null, null, null),
    THUMPER("Thumper", Rarity.COMMON, null, null, null),
    TICKY("Ticky", Rarity.COMMON, null, null, null),
    TOBI("Tobi", Rarity.COMMON, null, null, null),
    WEBB("Webb", Rarity.COMMON, null, "Spider's Den", "Find §a15 §7unique egg locations in the §c⏣ Spider's Den§7."),
    WILLIAM("William", Rarity.COMMON, null, null, null),
    WINSTON("Winston", Rarity.COMMON, null, null, null),
    WOODY("Woody", Rarity.COMMON, null, "The Park", "Find §a15 §7unique egg locations in the §a⏣ The Park§7."),
    ZACK("Zack", Rarity.COMMON, null, null, null),
    ZEA("Zea", Rarity.COMMON, null, "The End", "Find §a15 §7unique egg locations in §d⏣ The End§7."),

    // ==================== UNCOMMON RABBITS (125) ====================
    ABI("Abi", Rarity.UNCOMMON, "Obtained through getting called by §aHoppity§7.", null, null),
    ABIGAIL("Abigail", Rarity.UNCOMMON, null, null, null),
    ALEXA("Alexa", Rarity.UNCOMMON, null, null, null),
    ALEXANDER("Alexander", Rarity.UNCOMMON, null, null, null),
    ALPACA("Alpaca", Rarity.UNCOMMON, null, null, null),
    AMAZON("Amazon", Rarity.UNCOMMON, null, null, null),
    ARACHNO("Arachno", Rarity.UNCOMMON, null, null, "Defeat §cTarantula Broodfather Tier III§7."),
    ASHES("Ashes", Rarity.UNCOMMON, null, null, null),
    ASTERIX("Asterix", Rarity.UNCOMMON, null, null, null),
    ATTILA("Attila", Rarity.UNCOMMON, null, "Crimson Isle", null),
    AVERAGE("Average", Rarity.UNCOMMON, null, null, "Collect §a100 §7unique §fCOMMON §7Rabbits."),
    AZURE("Azure", Rarity.UNCOMMON, null, "Deep Caverns", null),
    BAMBAM("Bambam", Rarity.UNCOMMON, null, null, null),
    BANDIT("Bandit", Rarity.UNCOMMON, null, null, null),
    BARCODE("Barcode", Rarity.UNCOMMON, null, null, null),
    BENJI("Benji", Rarity.UNCOMMON, null, null, null),
    BETA("Beta", Rarity.UNCOMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    BILBO("Bilbo", Rarity.UNCOMMON, null, null, null),
    BLOSSOM("Blossom", Rarity.UNCOMMON, null, null, null),
    BLUEBERRY("Blueberry", Rarity.UNCOMMON, null, null, null),
    BRICK("Brick", Rarity.UNCOMMON, null, "Gold Mine", null),
    BRUTUS("Brutus", Rarity.UNCOMMON, null, null, null),
    BUBBLES("Bubbles", Rarity.UNCOMMON, null, null, null),
    BUCKWHEAT("Buckwheat", Rarity.UNCOMMON, null, null, null),
    BUFFALO("Buffalo", Rarity.UNCOMMON, null, null, null),
    BUGS("Bugs", Rarity.UNCOMMON, null, null, null),
    BUMPER("Bumper", Rarity.UNCOMMON, null, null, null),
    BUSTER("Buster", Rarity.UNCOMMON, null, null, null),
    BUTTERS("Butters", Rarity.UNCOMMON, null, null, null),
    CANDI("Candi", Rarity.UNCOMMON, null, null, null),
    CARTER("Carter", Rarity.UNCOMMON, null, null, null),
    CASPER("Casper", Rarity.UNCOMMON, null, null, null),
    CASSIDY("Cassidy", Rarity.UNCOMMON, null, null, null),
    CHARMIN("Charmin", Rarity.UNCOMMON, null, null, null),
    CHEWY("Chewy", Rarity.UNCOMMON, null, null, null),
    CHILLI("Chilli", Rarity.UNCOMMON, null, null, null),
    CHUBBY("Chubby", Rarity.UNCOMMON, null, null, null),
    CLOUDY("Cloudy", Rarity.UNCOMMON, null, null, null),
    COOKIE("Cookie", Rarity.UNCOMMON, null, null, null),
    COOPER("Cooper", Rarity.UNCOMMON, null, null, null),
    COTTON("Cotton", Rarity.UNCOMMON, null, null, null),
    COTTON_PUFF("Cotton Puff", Rarity.UNCOMMON, null, null, null),
    COTTONBALL("Cottonball", Rarity.UNCOMMON, null, null, null),
    DALTON("Dalton", Rarity.UNCOMMON, null, null, null),
    DANDELION("Dandelion", Rarity.UNCOMMON, null, null, null),
    DARLA("Darla", Rarity.UNCOMMON, null, null, null),
    DASH("Dash", Rarity.UNCOMMON, null, null, null),
    DEMARCUS("Demarcus", Rarity.UNCOMMON, null, null, null),
    DEMETRIOUS("Demetrious", Rarity.UNCOMMON, null, null, null),
    DESTINY("Destiny", Rarity.UNCOMMON, null, null, null),
    DOMINO("Domino", Rarity.UNCOMMON, null, null, null),
    DONKEY("Donkey", Rarity.UNCOMMON, null, "Backwater Bayou", null),
    EASTWOOD("Eastwood", Rarity.UNCOMMON, null, null, null),
    ELLA("Ella", Rarity.UNCOMMON, null, null, null),
    ENDER("Ender", Rarity.UNCOMMON, null, "The End", null),
    FIGGY("Figgy", Rarity.UNCOMMON, null, "Galatea", null),
    FITCH("Fitch", Rarity.UNCOMMON, null, null, null),
    FLIP_FLOP("Flip Flop", Rarity.UNCOMMON, null, null, null),
    FORREST("Forrest", Rarity.UNCOMMON, null, null, null),
    FUDGE_FOUNTAIN("Fudge Fountain", Rarity.UNCOMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    GADGET("Gadget", Rarity.UNCOMMON, null, null, null),
    GEE_GEE("Gee-Gee", Rarity.UNCOMMON, null, null, null),
    GINGER_GLAZE("Ginger Glaze", Rarity.UNCOMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    GOOFY("Goofy", Rarity.UNCOMMON, null, null, null),
    GROG("Grog", Rarity.UNCOMMON, null, "Crystal Hollows", null),
    HALMENROG("Halmenrog", Rarity.UNCOMMON, null, "Dwarven Mines", null),
    HARMONY("Harmony", Rarity.UNCOMMON, null, null, null),
    HONEY_HAZELNUT("Honey Hazelnut", Rarity.UNCOMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    HOP_A_LONG("Hop-a-long", Rarity.UNCOMMON, null, null, null),
    ICING_IVY("Icing Ivy", Rarity.UNCOMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    INFERNO("Inferno", Rarity.UNCOMMON, null, null, "Defeat §cInferno Demonlord Tier III§7."),
    IRENA("Irena", Rarity.UNCOMMON, null, null, null),
    JASMINE_JELLO("Jasmine Jello", Rarity.UNCOMMON, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    JAZZ("Jazz", Rarity.UNCOMMON, null, null, null),
    JELLY_BEAN("Jelly Bean", Rarity.UNCOMMON, null, null, null),
    KOBI("Kobi", Rarity.UNCOMMON, null, null, null),
    LEOPOLD("Leopold", Rarity.UNCOMMON, null, null, null),
    LULU("Lulu", Rarity.UNCOMMON, null, null, null),
    MAYBELLINE("Maybelline", Rarity.UNCOMMON, null, null, null),
    MEDIOCRITY("Mediocrity", Rarity.UNCOMMON, null, null, "Collect §a200 §7unique §fCOMMON §7Rabbits."),
    MELON("Melon", Rarity.UNCOMMON, null, "The Farming Islands", null),
    MILO("Milo", Rarity.UNCOMMON, null, null, null),
    MODEST("Modest", Rarity.UNCOMMON, null, null, "Collect §a50 §7unique §aUNCOMMON §7Rabbits."),
    MORGAN("Morgan", Rarity.UNCOMMON, null, null, null),
    OAKLEY("Oakley", Rarity.UNCOMMON, null, null, null),
    OAKLY("Oakly", Rarity.UNCOMMON, null, "Hub", null),
    OBELIX("Obelix", Rarity.UNCOMMON, null, null, null),
    ORCHID("Orchid", Rarity.UNCOMMON, null, "The Park", null),
    OREO("Oreo", Rarity.UNCOMMON, null, null, null),
    OTTO("Otto", Rarity.UNCOMMON, null, null, null),
    OZWALD("Ozwald", Rarity.UNCOMMON, null, null, null),
    PANCAKE("Pancake", Rarity.UNCOMMON, null, null, null),
    PATCHES("Patches", Rarity.UNCOMMON, null, null, null),
    PENELOPE("Penelope", Rarity.UNCOMMON, null, null, null),
    PEPSI("Pepsi", Rarity.UNCOMMON, null, null, null),
    PILLSBURY("Pillsbury", Rarity.UNCOMMON, null, null, null),
    POLKA_DOT("Polka Dot", Rarity.UNCOMMON, null, null, null),
    PORSCHE("Porsche", Rarity.UNCOMMON, null, null, null),
    PRESTIGE("Prestige", Rarity.UNCOMMON, null, null, "Reach §6Chocolate Factory III§7."),
    PRETZEL("Pretzel", Rarity.UNCOMMON, null, null, null),
    PRUDENT("Prudent", Rarity.UNCOMMON, null, null, "Collect §a100 §7unique §aUNCOMMON §7Rabbits."),
    QUINCY("Quincy", Rarity.UNCOMMON, null, null, null),
    RAVEN("Raven", Rarity.UNCOMMON, null, null, null),
    RINGO("Ringo", Rarity.UNCOMMON, null, null, null),
    RUINA("Ruina", Rarity.UNCOMMON, null, "Dungeon Hub", null),
    RUSTY("Rusty", Rarity.UNCOMMON, null, null, null),
    SARGENT("Sargent", Rarity.UNCOMMON, null, null, null),
    SEINFIELD("Seinfield", Rarity.UNCOMMON, null, null, null),
    SNOOPY("Snoopy", Rarity.UNCOMMON, null, null, null),
    SPRINKLES("Sprinkles", Rarity.UNCOMMON, null, null, null),
    SQUISH("Squish", Rarity.UNCOMMON, null, null, "Find §a500 §7duplicate Rabbits."),
    STEWART("Stewart", Rarity.UNCOMMON, null, null, null),
    SVEN("Sven", Rarity.UNCOMMON, null, null, "Defeat §9Sven Packmaster Tier III§7."),
    SWEETPEA("Sweetpea", Rarity.UNCOMMON, null, null, null),
    SYLVESTER("Sylvester", Rarity.UNCOMMON, null, null, null),
    THETA("Theta", Rarity.UNCOMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    TOBY("Toby", Rarity.UNCOMMON, null, null, null),
    TRIXIE("Trixie", Rarity.UNCOMMON, null, null, null),
    UNA("Una", Rarity.UNCOMMON, null, null, null),
    UPSILON("Upsilon", Rarity.UNCOMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    VOID("Void", Rarity.UNCOMMON, null, null, "Defeat §5Voidgloom Seraph Tier III§7."),
    WADSWORTH("Wadsworth", Rarity.UNCOMMON, null, null, null),
    WAFFLE("Waffle", Rarity.UNCOMMON, null, null, null),
    XI("Xi", Rarity.UNCOMMON, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    ZOMBIE("Zombie", Rarity.UNCOMMON, null, null, "Defeat §5Revenant Horror Tier III§7."),

    // ==================== RARE RABBITS (82) ====================
    ALADDIN("Aladdin", Rarity.RARE, null, null, null),
    ALOYSIUS("Aloysius", Rarity.RARE, null, null, null),
    BARBIE("Barbie", Rarity.RARE, null, null, null),
    BISHOP("Bishop", Rarity.RARE, null, null, null),
    BLACKBERRY("Blackberry", Rarity.RARE, null, null, null),
    BLACKJACK("Blackjack", Rarity.RARE, null, null, null),
    BOULDER("Boulder", Rarity.RARE, null, "Gold Mine", null),
    BUGATTI("Bugatti", Rarity.RARE, null, null, null),
    BUN_BUN("Bun Bun", Rarity.RARE, null, null, null),
    BURST("Burst", Rarity.RARE, null, null, "Find §a1,000 §7duplicate Rabbits."),
    CAJUN("Cajun", Rarity.RARE, null, null, null),
    CANOPUS("Canopus", Rarity.RARE, null, "The Park", null),
    CARAMEL("Caramel", Rarity.RARE, null, null, null),
    CARMINE("Carmine", Rarity.RARE, null, "Deep Caverns", null),
    CASANOVA("Casanova", Rarity.RARE, null, null, null),
    CHEVY("Chevy", Rarity.RARE, null, null, null),
    CINNAMON("Cinnamon", Rarity.RARE, null, null, null),
    COLTRON("Coltron", Rarity.RARE, null, "Crystal Hollows", null),
    CRYSTAL("Crystal", Rarity.RARE, null, null, null),
    DALLAS("Dallas", Rarity.RARE, null, null, null),
    DRACO("Draco", Rarity.RARE, null, null, null),
    EASTER("Easter", Rarity.RARE, null, null, null),
    EGG("Egg", Rarity.RARE, null, "The End", null),
    ELVIS("Elvis", Rarity.RARE, null, null, null),
    FAVOR("Favor", Rarity.RARE, null, null, "Collect §a50 §7unique §9RARE §7Rabbits."),
    FIGARO("Figaro", Rarity.RARE, null, null, null),
    FISH_THE_RABBIT("Fish the Rabbit", Rarity.RARE, "Obtained by finding the §6Stray Rabbit §7in the Chocolate Factory menu.", null, null),
    FRODO("Frodo", Rarity.RARE, null, null, null),
    GAMMA("Gamma", Rarity.RARE, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    GRAVLER("Gravler", Rarity.RARE, null, "Spider's Den", null),
    GREMLIN("Gremlin", Rarity.RARE, null, null, null),
    HONEY("Honey", Rarity.RARE, null, null, null),
    HOPE("Hope", Rarity.RARE, null, null, null),
    HYDE("Hyde", Rarity.RARE, null, null, null),
    IOTA("Iota", Rarity.RARE, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    JASPER("Jasper", Rarity.RARE, null, null, null),
    JYNX("Jynx", Rarity.RARE, null, null, null),
    KALUMDAI("Kalumdai", Rarity.RARE, null, "Dwarven Mines", null),
    KIWI_KISS("Kiwi Kiss", Rarity.RARE, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    LAVENDER_LEMON("Lavender Lemon", Rarity.RARE, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    LINUS("Linus", Rarity.RARE, null, null, null),
    LUMIE("Lumie", Rarity.RARE, null, "Galatea", null),
    MAPLE_MIRAGE("Maple Mirage", Rarity.RARE, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    MIDNIGHT("Midnight", Rarity.RARE, null, null, null),
    MONALISA("Monalisa", Rarity.RARE, null, null, null),
    MURPHY("Murphy", Rarity.RARE, null, null, null),
    MUSH_MUSH("Mush Mush", Rarity.RARE, null, "Dungeon Hub", null),
    MUTONIO("Mutonio", Rarity.RARE, null, "The Farming Islands", null),
    NEPTUNE("Neptune", Rarity.RARE, null, null, null),
    NOUGAT_NEBULA("Nougat Nebula", Rarity.RARE, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    OLYMPE("Olympe", Rarity.RARE, null, null, null),
    OMICRON("Omicron", Rarity.RARE, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    ONYX("Onyx", Rarity.RARE, null, null, null),
    ORANGE_OBSIDIAN("Orange Obsidian", Rarity.RARE, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    ORLANDO("Orlando", Rarity.RARE, null, null, null),
    PADDINGTON("Paddington", Rarity.RARE, null, null, null),
    PEANUT("Peanut", Rarity.RARE, null, null, null),
    PEPPER("Pepper", Rarity.RARE, null, null, null),
    PHANTOM("Phantom", Rarity.RARE, null, null, null),
    PHI("Phi", Rarity.RARE, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    POPCORN("Popcorn", Rarity.RARE, null, null, null),
    PRIDE("Pride", Rarity.RARE, null, null, null),
    PUMPKIN("Pumpkin", Rarity.RARE, null, null, null),
    RIVER("River", Rarity.RARE, null, null, null),
    SAGE("Sage", Rarity.RARE, null, null, null),
    SHERK("Sherk", Rarity.RARE, null, "Backwater Bayou", null),
    SNOWBALL("Snowball", Rarity.RARE, null, null, null),
    SPIRIT("Spirit", Rarity.RARE, null, null, null),
    SPOOKY("Spooky", Rarity.RARE, null, null, null),
    STORMY("Stormy", Rarity.RARE, null, null, null),
    SULFUR("Sulfur", Rarity.RARE, null, "Crimson Isle", null),
    SUNNY("Sunny", Rarity.RARE, null, null, null),
    TORNADO("Tornado", Rarity.RARE, null, null, null),
    TOWNY("Towny", Rarity.RARE, null, "Hub", null),
    TRICKY("Tricky", Rarity.RARE, null, null, null),
    UNCLE_BUCK("Uncle Buck", Rarity.RARE, null, null, null),
    VLAD("Vlad", Rarity.RARE, null, null, null),
    WESSON("Wesson", Rarity.RARE, null, null, null),
    WIDGET("Widget", Rarity.RARE, null, null, null),
    WILLOW("Willow", Rarity.RARE, null, null, null),
    ZERO("Zero", Rarity.RARE, null, null, null),

    // ==================== EPIC RABBITS (45) ====================
    ACE("Ace", Rarity.EPIC, null, null, null),
    ACHILLES("Achilles", Rarity.EPIC, null, null, null),
    ALPINE("Alpine", Rarity.EPIC, null, null, null),
    ANGEL("Angel", Rarity.EPIC, null, null, null),
    CALYPSO("Calypso", Rarity.EPIC, null, null, null),
    CASTLE("Castle", Rarity.EPIC, null, "Hub", null),
    CHI("Chi", Rarity.EPIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    COMET("Comet", Rarity.EPIC, null, null, null),
    DELTA("Delta", Rarity.EPIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    DOJO("Dojo", Rarity.EPIC, null, "Crimson Isle", null),
    EISEN("Eisen", Rarity.EPIC, null, "Crimson Isle", null),
    GATSBY("Gatsby", Rarity.EPIC, null, null, null),
    GROVE("Grove", Rarity.EPIC, null, "Galatea", null),
    HOLE("Hole", Rarity.EPIC, null, "Gold Mine", null),
    IMPLODE("Implode", Rarity.EPIC, null, null, "Find §a2,000 §7duplicate Rabbits."),
    JEDI("Jedi", Rarity.EPIC, null, null, null),
    KAPPA("Kappa", Rarity.EPIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    KEN("Ken", Rarity.EPIC, null, null, null),
    KIERA("Kiera", Rarity.EPIC, null, null, null),
    KODO("Kodo", Rarity.EPIC, null, null, null),
    KRYS("Krys", Rarity.EPIC, null, "Dungeon Hub", null),
    MAXIMA("Maxima", Rarity.EPIC, null, "Spider's Den", null),
    MERLIN("Merlin", Rarity.EPIC, null, null, null),
    NEARQUAAD("Nearquaad", Rarity.EPIC, null, "Backwater Bayou", null),
    NECRON("Necron", Rarity.EPIC, null, null, "§cThe Catacombs Floor VII §7Completion"),
    OCTAVIA("Octavia", Rarity.EPIC, null, "Dwarven Mines", null),
    PEARL("Pearl", Rarity.EPIC, null, "The End", null),
    PEPPERMINT_PEARL("Peppermint Pearl", Rarity.EPIC, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    PI("Pi", Rarity.EPIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    PRINCE("Prince", Rarity.EPIC, null, null, null),
    PUNCH("Punch", Rarity.EPIC, null, null, null),
    QUINCE_QUARK("Quince Quark", Rarity.EPIC, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    RAMBO("Rambo", Rarity.EPIC, null, null, null),
    RASPBERRY_RIPPLE("Raspberry Ripple", Rarity.EPIC, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    ROCHIE("Rochie", Rarity.EPIC, null, "The Farming Islands", null),
    SAGA("Saga", Rarity.EPIC, null, null, "Collect §a25 §7unique §5EPIC §7Rabbits."),
    SAVANNAH("Savannah", Rarity.EPIC, null, "The Park", null),
    SIMBA("Simba", Rarity.EPIC, null, null, null),
    STRAWBERRY_SWIRL("Strawberry Swirl", Rarity.EPIC, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    THOR("Thor", Rarity.EPIC, null, null, null),
    TOFFEE_TULIP("Toffee Tulip", Rarity.EPIC, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    TRIX("Trix", Rarity.EPIC, null, null, null),
    TURBO("Turbo", Rarity.EPIC, null, null, null),
    VIOLET("Violet", Rarity.EPIC, null, "Crystal Hollows", null),

    // ==================== LEGENDARY RABBITS (24) ====================
    APOLLO("Apollo", Rarity.LEGENDARY, null, null, null),
    APRIL("April", Rarity.LEGENDARY, null, null, null),
    ATLAS("Atlas", Rarity.LEGENDARY, null, null, null),
    ECHO("Echo", Rarity.LEGENDARY, null, null, null),
    EL_DORADO("El Dorado", Rarity.LEGENDARY, "Obtained by finding the §6Stray Rabbit §7in the Chocolate Factory menu.", null, null),
    EPSILON("Epsilon", Rarity.LEGENDARY, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    EXTRA("Extra", Rarity.LEGENDARY, null, null, "Collect §a20 §7unique §6LEGENDARY §7Rabbits."),
    GENERAL("General", Rarity.LEGENDARY, null, null, null),
    HOUDINI("Houdini", Rarity.LEGENDARY, null, null, null),
    KAEMAN("Kaeman", Rarity.LEGENDARY, null, null, "§cMaster Mode The Catacombs Floor VII §7Completion"),
    LAMBDA("Lambda", Rarity.LEGENDARY, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    MAGIC("Magic", Rarity.LEGENDARY, null, null, null),
    MYSTIC("Mystic", Rarity.LEGENDARY, null, null, null),
    NOVA("Nova", Rarity.LEGENDARY, null, null, null),
    PSI("Psi", Rarity.LEGENDARY, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    RHO("Rho", Rarity.LEGENDARY, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    SHADOW("Shadow", Rarity.LEGENDARY, null, null, null),
    SOLOMON("Solomon", Rarity.LEGENDARY, null, null, null),
    STORM("Storm", Rarity.LEGENDARY, null, null, null),
    UBE_UNICORN("Ube Unicorn", Rarity.LEGENDARY, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    VANILLA_VORTEX("Vanilla Vortex", Rarity.LEGENDARY, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    WALNUT_WHIRL("Walnut Whirl", Rarity.LEGENDARY, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    XOCO_XANUDU("Xoco Xanudu", Rarity.LEGENDARY, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    YOGURT_YUCCA("Yogurt Yucca", Rarity.LEGENDARY, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),

    // ==================== MYTHIC RABBITS (11) ====================
    DANTE("Dante", Rarity.MYTHIC, null, null, null),
    EINSTEIN("Einstein", Rarity.MYTHIC, null, null, null),
    GALAXY("Galaxy", Rarity.MYTHIC, null, null, null),
    KING("King", Rarity.MYTHIC, null, null, null),
    MU("Mu", Rarity.MYTHIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    NAPOLEON("Napoleon", Rarity.MYTHIC, null, null, null),
    OMEGA("Omega", Rarity.MYTHIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    SIGMA("Sigma", Rarity.MYTHIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    ZEST_ZEPHYR("Zest Zephyr", Rarity.MYTHIC, "Obtained through your §6Chocolate Factory Milestones§7.", null, null),
    ZETA("Zeta", Rarity.MYTHIC, "Obtained through your §6Chocolate Shop Milestones§7.", null, null),
    ZORRO("Zorro", Rarity.MYTHIC, null, null, null),

    // ==================== DIVINE RABBITS (5) ====================
    AURORA("Aurora", Rarity.DIVINE, null, null, null),
    CELESTIA("Celestia", Rarity.DIVINE, null, null, null),
    ORION("Orion", Rarity.DIVINE, null, null, null),
    STARFIRE("Starfire", Rarity.DIVINE, null, null, null),
    VEGA("Vega", Rarity.DIVINE, null, null, null);

    private final String displayName;
    private final Rarity rarity;
    private final String obtainMethod;
    private final String location;
    private final String requirement;

    ChocolateRabbit(String displayName, Rarity rarity, String obtainMethod, String location, String requirement) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.obtainMethod = obtainMethod;
        this.location = location;
        this.requirement = requirement;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getObtainMethod() {
        return obtainMethod;
    }

    public String getLocation() {
        return location;
    }

    public String getRequirement() {
        return requirement;
    }

    public int getChocolateBonus() {
        return rarity.getChocolateBonus();
    }

    public double getMultiplierBonus() {
        return rarity.getMultiplierBonus();
    }

    public String getColorCode() {
        return rarity.getColorCode();
    }

    public String getFormattedName() {
        return "§" + rarity.getColorCode() + displayName;
    }

    /**
     * Gets the formatted location string with appropriate color
     */
    public String getFormattedLocation() {
        if (location == null) return null;
        return switch (location) {
            case "Crimson Isle" -> "§c⏣ Crimson Isle";
            case "Deep Caverns" -> "§b⏣ Deep Caverns";
            case "Backwater Bayou" -> "§2⏣ Backwater Bayou";
            case "Gold Mine" -> "§6⏣ Gold Mine";
            case "Hub" -> "§bHub";
            case "The Park" -> "§a⏣ The Park";
            case "Crystal Hollows" -> "§5⏣ Crystal Hollows";
            case "The End" -> "§d⏣ The End";
            case "Dungeon Hub" -> "§c⏣ Dungeon Hub";
            case "Dwarven Mines" -> "§2⏣ Dwarven Mines";
            case "The Farming Islands" -> "§eThe Farming Islands";
            case "Spider's Den" -> "§c⏣ Spider's Den";
            case "Galatea" -> "§2⏣ Galatea";
            default -> "§7" + location;
        };
    }

    /**
     * Gets the resident label with color for the location
     */
    public String getResidentLabel() {
        if (location == null) return null;
        return switch (location) {
            case "Crimson Isle" -> "§cCrimson Isle Resident";
            case "Deep Caverns" -> "§bDeep Caverns Resident";
            case "Backwater Bayou" -> "§2Backwater Bayou Resident";
            case "Gold Mine" -> "§6Gold Mine Resident";
            case "Hub" -> "§bHub Resident";
            case "The Park" -> "§aThe Park Resident";
            case "Crystal Hollows" -> "§5Crystal Hollows Resident";
            case "The End" -> "§dThe End Resident";
            case "Dungeon Hub" -> "§cDungeon Hub Resident";
            case "Dwarven Mines" -> "§2Dwarven Mines Resident";
            case "The Farming Islands" -> "§eThe Farming Islands Resident";
            case "Spider's Den" -> "§cSpider's Den Resident";
            case "Galatea" -> "§2Galatea Resident";
            default -> "§7" + location + " Resident";
        };
    }

    public static int getTotalRabbits() {
        return values().length;
    }

    public static int getRabbitCountByRarity(Rarity rarity) {
        int count = 0;
        for (ChocolateRabbit rabbit : values()) {
            if (rabbit.rarity == rarity) count++;
        }
        return count;
    }

    public enum Rarity {
        COMMON("f", "COMMON", 1, 0.002),
        UNCOMMON("a", "UNCOMMON", 2, 0.003),
        RARE("9", "RARE", 4, 0.004),
        EPIC("5", "EPIC", 10, 0.005),
        LEGENDARY("6", "LEGENDARY", 0, 0.02),
        MYTHIC("d", "MYTHIC", 0, 0.05),
        DIVINE("b", "DIVINE", 0, 0.03);

        private final String colorCode;
        private final String displayName;
        private final int chocolateBonus;
        private final double multiplierBonus;

        Rarity(String colorCode, String displayName, int chocolateBonus, double multiplierBonus) {
            this.colorCode = colorCode;
            this.displayName = displayName;
            this.chocolateBonus = chocolateBonus;
            this.multiplierBonus = multiplierBonus;
        }

        public String getColorCode() {
            return colorCode;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getChocolateBonus() {
            return chocolateBonus;
        }

        public double getMultiplierBonus() {
            return multiplierBonus;
        }

        public String getFormattedName() {
            return "§" + colorCode + "§l" + displayName + " RABBIT";
        }
    }
}
