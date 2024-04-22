package com.example.portfolio.market.pricing.store;

import com.example.portfolio.security.Security;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryPriceStoreTest {

    @Test
    void should_set_and_get_price() {
        Security security = Mockito.mock(Security.class);
        Mockito.when(security.getSymbol()).thenReturn("AAA");

        PriceStore store = new InMemoryPriceStore();
        store.setPrice(security, 110.0);

        assertEquals(110.0, store.getPrice(security));
    }

    @Test
    void should_return_zero_price_if_security_not_present() {
        Security security = Mockito.mock(Security.class);
        Mockito.when(security.getSymbol()).thenReturn("ABC");

        PriceStore store = new InMemoryPriceStore();
        assertEquals(0.0, store.getPrice(security));
    }

    @Test
    void should_update_price() {
        Security security = Mockito.mock(Security.class);
        Mockito.when(security.getSymbol()).thenReturn("DEF");

        PriceStore store = new InMemoryPriceStore();
        store.setPrice(security, 110.0);

        assertEquals(110.0, store.getPrice(security));

        store.setPrice(security, 120.0);

        assertEquals(120.0, store.getPrice(security));
    }
}
