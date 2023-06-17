package fr.atesab.atehud;

import java.util.List;

import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Session;

public class FakeItems2 extends Item {

	public static String[] list = { "BickerCraft", "Eclipsen::a squid", "LightHN::a sheep",
			"jellyhunter::a green slime", "Bud_Bundy::a bear", "shwiny95:: a enderman", "Calebis9::a spider",
			"Avaris11::a hamburger", "Y_D_domino::Jack O'Lantern with hat", "sleepiM1::pedobear",
			"RedPegasus::note block", "3423Kyle::trollface", "Itz_Assasin::Red Power Ranger",
			"chickolympics:: a different chicken head", "omittingbread::pig skull", "neillyken::TMNT purple face",
			"Superyoshibros::Yoshi", "BaglesMan::Green Power Ranger", "msalihov:: Working man",
			"Koeng101::Stormtrooper", "ARA275::Default Pokemon Trainer", "ghinterm::Cactus",
			"Samsam_Momo::Jake from Adventure Time", "MoulaTime::Grass head", "anamericandude::Wood head",
			"natejh::Pikachu", "JawaJish14::Chewbacca", "jakeypoo2005::Panda bear", "Merten123::Jack Sparrow",
			"EBOS23::Lava", "vennos93::Jigsaw", "spongebobtime2::Sub-Zero", "Scemm::Dispenser", "m0ose12321::Moose",
			"awsobuscus::Minecraft chicken", "Budwolf::Wolf", "pat2424::Walrus", "Esonicspeedster::Sonic the Hedgegog",
			"MrPeePeeCow::Domo", "henrik811::Pig", "gnhc::PSY", "tiger9a::Tiger", "erwintrude::another Chicken",
			"Mr_OfficeCreeper:: The Joker", "morgans567::Zombie Pigman", "Poketostorm::Golden Steve Face",
			"safo::Iron Golem", "Freyr29::Donald Duck", "LycleLink::Penguin", "Herobrine::herobrine duh",
			"Natalieisawesome::a bunny", "rhyslarson::Batman", "heinzensmeinzen::ugly dude", "wolfgriffe::another wolf",
			"BoomerMan98:: bricks", "Skipper3210::clown", "blader1176::? sign", "Ahiya::mudkip",
			"artseefartsee::monkey", "purplehayes::Wolverine", "iBlazeXrayZ::Blaze", "enaircf:: Pencil eraser",
			"SquareHD::a dice [probably]", "semmieeeeee::Blue block", "crafterkid1k8::Ghast", "kyleman747::Grinch",
			"Comcastt::Front Squid Face", "sho2go27::Shiny blue light", "lancaster98::Companion Cube from Portal",
			"halo 99900::Black block", "lobwotscha::Wooden plank", "Zawern::Pumpkin",
			"ChoclateMuffin::a chocolate muffin", "edabonacci::Cobblestone", "spiriti1::Green monster eye",
			"pablo_asparagus::Nyan Cat", "Cheemz::Spider-man", "zsoccer23::Red angry bird", "kongHD::a monkey",
			"DeFrank::Donkey Kong", "andysam1999::Elmo", "pyrotnt1::Monster mouth", "hugge75::fox",
			"Becquerine::Redstone lamp", "HCTNT::TNT block", "IzanV::Kratos", "NiXWorld::a different Zombie",
			"forrynh::wood puppet", "nixzpatel::Naruto", "CraftingFire::Deadpool", "AGGRO965::another Deadpool",
			"begz::another penguin", "mrmaxfbk::another monster eye", "ZitterNipple::Duck", "Matt_5X4::Master Chief",
			"boyfromhell43::Viking", "LobsterDust::bullseye", "Solid_Snake3::Solid Snake", "Human_Kirby::Kirby",
			"ebiddytwister::Link", "Axterin::Connor Kenway", "Fransicodd::Blue Power Ranger",
			"Keanulaszlo::Crash Bandicoot", "Barnyard_Owl::Owl", "daminecraftninja::Ninja",
			"Azilizan::another Spider-Man", "Goodle::snowman", "TetroSpekt::robot", "Creem7116::Realistic man face",
			"Trusted23::another Solid Snake", "Explosion_936::Captain America", "roryo8::Falco Lombardi",
			"wetodd15::Old Herobrine", "ThunderRay34::Tron Helm", "jesusismyhomie::Old Man",
			"nicariox42::HD Mario Bros.", "NanobiteNpc::Rude Man", "samsamsam1234::Pirate", "lymibom::Elegant Man",
			"platypus99::Irish dwarf", "SuperGenoXP::Genie", "Monkeycapers::Cute Monkey", "fancypants39::Old Snake",
			"gothreaux::Lizard-Man", "JoeCMGIS::Dwarf", "Exeldoh::Turkey", "Kam627::Pink Power Ranger",
			"YObabewassup::somekind of Demon", "Villager::villager", "BigBadW0lf::Wolf", "flyman821::Guy with goggles",
			"xFlyier::Half human, half robot", "cam77890::Blue cow", "Pixology::No TV Signal",
			"MisterLamster::Charmander", "marvelousammar::Old Steve", "BabsHD::Lava Monster",
			"Nelson540::Captain America", "Linkwollf::Venom", "Preloom::Purple Cow", "TheVideoWiz::Bane",
			"epichickenuggets::Rainbow Dash", "General404::Knight", "TininchoMC::Ghast", "Robinho1502::Minion",
			"JoelPersson7::another minion", "_Trespassing_::Dumb face", "Crazybloxer::Dumb face with helmet",
			"ThePintor::Dumb and ugly face", "LegendzOfHoboz::another Dumb and ugly face", "citro30::The Joker",
			"burai564::Another hamburger", "king601::another penguin", "ferrase::Black Skull", "ptrinadadn::Wally",
			"The_SkittleZ::Guy that looks like Rambo", "Sugar_Cane_::Sugar Cane", "carterpaul::Platypus",
			"BJmatba::Blue light", "trolex213::Dark Purple Creeper", "unhappyworld::Dark Blue Creeper",
			"Scamalascazzu::Jiraiya", "facucarello12::Chuck Norris", "Praetoriian::Knight of Light",
			"chubbyderginger::Sun & Sky", "SHOCKN3SSK1D::Luffy", "Trusted23::Zombie", "Volc_Guy::Blaziken",
			"ThePoup::Clown", "Shinkao::Stitch", "Blast186::Sasuke Uchiha", "CandleGlow::Green Lantern",
			"zslorca::? Block" },
			vip = { "Aypierre", "Aurelien_Sama", "Roi_Louis", "Wotan", "DavLec", "Zephirr", "BillSilverlight",
					"Thefantasio974", "Thoyy", "Relient", "Edorock", "ShoukaSeikyo", "Fancois_Sama", "Notch", "Jeb_",
					"Siphano", "Zer4toR", "nemenems", "Mumbo", "_CrazyP_", "bspkrs", "C418", "Dinnerbone", "Grumm",
					"AntVenom", "pewdie", "CaptainSparklez", "Lunatrius", "Lycanite", "GregoriusT", "Etho", "SethBling",
					"direwolf20", "Bacon_Donut", "xlson", "JustePourJouer" },
			mhf = { "MHF_Alex::(MHF)", "MHF_Blaze::(MHF)", "MHF_CaveSpider::(MHF)", "MHF_Chicken::(MHF)",
					"MHF_Cow::(MHF)", "MHF_Creeper::(MHF)", "MHF_Enderman::(MHF)", "MHF_Ghast::(MHF)",
					"MHF_Herobrine::(MHF)", "MHF_Golem::(MHF)", "MHF_LavaSlime::(MHF)", "MHF_MushroomCow::(MHF)",
					"MHF_Ocelot::(MHF)", "MHF_Pig::(MHF)", "MHF_PigZombie::(MHF)", "MHF_Sheep::(MHF)",
					"MHF_Skeleton::(MHF)", "MHF_Slime::(MHF)", "MHF_Spider::(MHF)", "MHF_Squid::(MHF)",
					"MHF_Steve::(MHF)", "MHF_Villager::(MHF)", "MHF_WSkeleton::(MHF)", "MHF_Zombie::(MHF)",
					"MHF_Cactus::(MHF)", "MHF_TNT::(MHF)", "MHF_TNT2::(MHF)", "MHF_Cake::(MHF)", "MHF_Chest::(MHF)",
					"MHF_CoconutB::(MHF)", "MHF_CoconutG::(MHF)", "MHF_Melon::(MHF)", "MHF_OakLog::(MHF)",
					"MHF_Present1::(MHF)", "MHF_Present2::(MHF)", "MHF_Pumpkin::(MHF)", "MHF_ArrowUp::(MHF)",
					"MHF_ArrowDown::(MHF)", "MHF_ArrowLeft::(MHF)", "MHF_ArrowRight::(MHF)", "MHF_Exclamation::(MHF)",
					"MHF_Question::(MHF)" };

	public FakeItems2() {
		setCreativeTab(ModMain.ATEcreativeTAB2);
		setHasSubtypes(true);
	}
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		if(ModMain.genFake2 && tab != CreativeTabs.tabAllSearch) {
			subItems = ItemHelp.addTitle(subItems, I18n.format("act.devfriend"));
			subItems.add(ItemHelp.barrier); // 1
			subItems.add(ItemHelp.getHead("ATE47", "(Dev)")); // 2
			subItems.add(ItemHelp.getHead("DarkArthurCT", "(El Tardos)")); // 3
			subItems.add(ItemHelp.getHead("Goufenard", "(Potato)")); // 4
			subItems.add(ItemHelp.getHead("PikSel42", "(Babycraft Owner)")); // 5
			subItems.add(ItemHelp.getHead("Spktacular", "(Dev's head friend)")); // 6
			subItems.add(ItemHelp.getHead("Paralogos")); // 7
			subItems.add(ItemHelp.getHead(Minecraft.getMinecraft().getSession().getUsername())); // 8
			subItems.add(ItemHelp.barrier); // 9
			subItems = ItemHelp.addGroup(subItems, vip, I18n.format("act.vipdev"));
			subItems = ItemHelp.addGroup(subItems, mhf, I18n.format("act.mhf"));
			subItems = ItemHelp.addGroup(subItems, list, I18n.format("act.list"));
		} else subItems.add(ItemHelp.noGen);
	}
}
