/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import java.util.HashSet;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Chill;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Frost;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfFireblast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Fire;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ElementalSprite;
import com.watabou.utils.Random;

public class Elemental extends Mob {

	{
		name = "fire elemental";
		spriteClass = ElementalSprite.class;
		
		HP = HT = 65;
		defenseSkill = 20;
		atkSkill = 25;
		dmgRed = 5;
		dmgMin = 16;
		dmgMax = 20;

		EXP = 10;
		maxLvl = 24;
		
		flying = true;
		
		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;
		mobType = MOBTYPE_DEBUFF;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Burning.class ).reignite( enemy );
		}
		
		return damage;
	}
	
	@Override
	public void add( Buff buff ) {
		if (buff instanceof Burning) {
			if (HP < HT) {
				HP++;
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
		} else if (buff instanceof Frost || buff instanceof Chill) {
				if (Level.water[this.pos])
					damage( Random.NormalIntRange( HT / 2, HT ), buff );
				else
					damage( Random.NormalIntRange( 1, HT * 2 / 3 ), buff );
		} else {
			super.add( buff );
		}
	}
	
	@Override
	public String description() {
		return
			"Wandering fire elementals are a byproduct of summoning greater entities. " +
			"They are too chaotic in their nature to be controlled by even the most powerful demonologist.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Burning.class );
		IMMUNITIES.add( Fire.class );
		IMMUNITIES.add( WandOfFireblast.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
