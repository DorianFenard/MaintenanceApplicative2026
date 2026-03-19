package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        if (items == null) return;
        for (Item item : items) {
            if (item == null) continue;

            if (isSulfuras(item)) {
                continue;
            }
            if (isAgedBrie(item)) {
                increaseQuality(item, 1);
            } else if (isBackstage(item)) {
                increaseQuality(item, 1);
                if (item.sellIn < 11) {
                    increaseQuality(item, 1);
                }
                if (item.sellIn < 6) {
                    increaseQuality(item, 1);
                }
            } else {
                decreaseQuality(item, 1);
            }

            item.sellIn = item.sellIn - 1;

            if (item.sellIn < 0) {
                if (isAgedBrie(item)) {
                    increaseQuality(item, 1);
                } else if (isBackstage(item)) {
                    setQuality(item, 0);
                } else {
                    decreaseQuality(item, 1);
                }
            }
        }
    }

    private boolean isAgedBrie(Item item) {
        return "Aged Brie".equals(item.name);
    }

    private boolean isBackstage(Item item) {
        return "Backstage passes to a TAFKAL80ETC concert".equals(item.name);
    }

    private boolean isSulfuras(Item item) {
        return "Sulfuras, Hand of Ragnaros".equals(item.name);
    }

    private void increaseQuality(Item item, int amount) {
        if (item.quality < 50) {
            item.quality = Math.min(50, item.quality + amount);
        }
    }

    private void decreaseQuality(Item item, int amount) {
        if (item.quality > 0) {
            item.quality = Math.max(0, item.quality - amount);
        }
    }

    private void setQuality(Item item, int value) {
        item.quality = Math.max(0, Math.min(50, value));
    }
}
