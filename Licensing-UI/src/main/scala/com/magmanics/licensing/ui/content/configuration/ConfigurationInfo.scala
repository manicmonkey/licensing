package com.magmanics.licensing.ui.content.configuration

import reflect.BeanInfo
import java.util.Date

/**
 * @author jbaxter - 20/04/11
 */
@BeanInfo
case class ConfigurationInfo(configurationId: Long, product: String, created: Date, user: String, activations: String, serial: String)