package org.moparscape.msc.gs.model;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

import org.moparscape.msc.config.Formulae;
import org.moparscape.msc.gs.Instance;
import org.moparscape.msc.gs.tools.DataConversions;

public class ActiveTile {

	private SoftReference<List<Player>> playerReference;
	private SoftReference<List<Npc>> npcReference;
	private SoftReference<List<Item>> itemReference;
	private SoftReference<GameObject> objectReference;
	private SoftReference<Integer> xReference, yReference;

	public ActiveTile(int x, int y) {
		this.playerReference = new SoftReference<List<Player>>(new LinkedList<Player>());
		this.npcReference = new SoftReference<List<Npc>>(new LinkedList<Npc>());
		this.itemReference = new SoftReference<List<Item>>(new LinkedList<Item>());
		this.xReference = new SoftReference<Integer>(x);
		this.yReference = new SoftReference<Integer>(y);
	}

	public void add(Entity entity) {
		if (entity instanceof Player) {
			playerReference.get().add((Player)entity);
		} else if (entity instanceof Npc) {
			npcReference.get().add((Npc)entity);
		} else if (entity instanceof Item) {
			itemReference.get().add((Item)entity);
		} else if (entity instanceof GameObject) {
			if (objectReference != null) {
				World.getWorld().unregisterGameObject(objectReference.get());
			}
			objectReference = new SoftReference<GameObject>((GameObject)entity);
		}
	}
	
	public void remove(Entity entity) {
		if (entity instanceof Player) {
			playerReference.get().remove(entity);
		} else if (entity instanceof Npc) {
			npcReference.get().remove(entity);
		} else if (entity instanceof Item) {
			itemReference.get().remove(entity);
		} else if (entity instanceof GameObject) {
			objectReference = null;
		}
	}
	
	public boolean hasPlayers() {
		return playerReference != null && playerReference.get().size() > 0;
	}

	public List<Player> getPlayers() {
		return playerReference.get();
	}

	public boolean hasGameObject() {
		return objectReference != null;
	}
	
	public GameObject getGameObject() {
		return objectReference.get();
	}
	
	public List<Item> getItems() {
		return itemReference.get();
	}
	
	public boolean hasItem(Item item) {
		return itemReference.get().contains(item);
	}
	
	public boolean hasItems() {
		return itemReference != null && itemReference.get().size() > 0;
	}

	public List<Npc> getNpcs() {
		return npcReference.get();
	}
	
	public boolean hasNpcs() {
		return npcReference != null && npcReference.get().size() > 0;
	}

	public int getX() {
		return xReference.get();
	}

	public int getY() {
		return yReference.get();
	}
	
	public boolean specificArea() {
		boolean t = DataConversions.inPointArray(Formulae.noremoveTiles, new Point(this.getX(), this.getY()));
		return t;
	}
	
	public void cleanItself() {
		if (!this.hasGameObject() && !this.hasItems() && !this.hasNpcs() && !this.hasPlayers() && !this.specificArea()) {
			Instance.getWorld().tiles[this.getX()][this.getY()] = null;
		}
	}

}