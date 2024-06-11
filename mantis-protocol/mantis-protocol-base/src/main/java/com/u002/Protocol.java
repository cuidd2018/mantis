package com.u002;

import com.u002.basic.Server;
import com.u002.core.extension.Spi;

@Spi
public interface Protocol {

    Server createServer();
}
