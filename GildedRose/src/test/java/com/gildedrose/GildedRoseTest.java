package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    /**
     * Test de l'article normal avant la date de vente 
     */
    @Test
    void normalItemBeforeSellDate() {
        Item[] items = new Item[] { new Item("foo", 1, 5) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo", app.items[0].name);
        assertEquals(0, app.items[0].sellIn);
        assertEquals(4, app.items[0].quality);
    }

    /**
     * Test de l'article normal le jour de la vente
     */
    @Test
    void normalItemOnSellDate() {
        Item[] items = new Item[] { new Item("foo", 0, 5) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(-1, app.items[0].sellIn);
        assertEquals(3, app.items[0].quality);
    }

    /**
     * Test de l'article normal après la date de vente
     */
    @Test
    void agedBrieIncreases() {
        Item[] items = new Item[] { new Item("Aged Brie", 2, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(1, app.items[0].sellIn);
        assertEquals(1, app.items[0].quality);
    }

    /**
     * Test de l'article normal après la date de vente
     */
    @Test
    void agedBrieAfterSellDateIncreasesTwice() {
        Item[] items = new Item[] { new Item("Aged Brie", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(-1, app.items[0].sellIn);
        assertEquals(2, app.items[0].quality);
    }

    /**
     * Test de l'article Sulfuras qui ne change jamais
     */
    @Test
    void sulfurasNeverChanges() {
        Item[] items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, app.items[0].sellIn);
        assertEquals(80, app.items[0].quality);
    }

    /**
     * Test de l'article Backstage Passes qui augmente de 1 avant 10 jours, de 2 entre 10 et 5 jours, de 3 entre 5 et 0 jours et qui tombe à 0 après le concert
     */
    @Test
    void backstageLongBeforeSellDate() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(14, app.items[0].sellIn);
        assertEquals(21, app.items[0].quality);
    }

    /**
     * Test de l'article Backstage Passes qui augmente de 1 avant 10 jours, de 2 entre 10 et 5 jours, de 3 entre 5 et 0 jours et qui tombe à 0 après le concert
     */
    @Test
    void backstageMediumCloseToSellDate() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(9, app.items[0].sellIn);
        assertEquals(22, app.items[0].quality);
    }

    /**
     * Test de l'article Backstage Passes qui augmente de 1 avant 10 jours, de 2 entre 10 et 5 jours, de 3 entre 5 et 0 jours et qui tombe à 0 après le concert
     */
    @Test
    void backstageVeryCloseToSellDate() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(4, app.items[0].sellIn);
        assertEquals(23, app.items[0].quality);
    }

    @Test
    void backstageAfterConcertQualityDropsToZero() {
        Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(-1, app.items[0].sellIn);
        assertEquals(0, app.items[0].quality);
    }

    /**
     * Test de l'article normal après la date de vente
     */
    @Test
    void qualityNeverNegative() {
        Item[] items = new Item[] { new Item("foo", 5, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, app.items[0].quality);
    }
/**
     * Test de l'article normal après la date de vente
     */
    @Test
    void qualityNeverExceeds50() {
        Item[] items = new Item[] { new Item("Aged Brie", 5, 50) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, app.items[0].quality);
    }
    /**
     * Test de la méthode toString de l'article
     */
    @Test
    void itemToString(){
        Item[] items = new Item[] { new Item("Aged Brie", 5, 50) };
        assertEquals("Aged Brie, 5, 50", items[0].toString());
    }

}
