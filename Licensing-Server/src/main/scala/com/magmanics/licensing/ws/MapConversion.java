/**
 * Magmanics Licensing. This web application allows for centralized control
 * of client application activation, with optional configuration parameters
 * to control licensable features, and storage of supplementary information
 * about the client machine. Client applications may interface with this
 * central server (for activation) using libraries licenced under an
 * alternative licence.
 *
 * Copyright (C) 2010 James Baxter <j.w.baxter(at)gmail.com>
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmanics.licensing.ws;

import com.magmanics.licensing.ws.model.Map;
import com.magmanics.licensing.ws.model.MapEntry;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.List;

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 10-Sep-2010
 */
public class MapConversion extends XmlAdapter<Map, HashMap<String, String>> {

    @Override
    public Map marshal(HashMap<String, String> map) throws Exception {
        Map xmlMap = new Map();
        List<MapEntry> entries = xmlMap.getEntry();
        for (java.util.Map.Entry<String, String> entry : map.entrySet()) {
            MapEntry mapEntry = new MapEntry();
            mapEntry.setKey(entry.getKey());
            mapEntry.setValue(entry.getValue());
            entries.add(mapEntry);
        }
        return xmlMap;
    }

    @Override
    public HashMap<String, String> unmarshal(Map xmlMap) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        for (MapEntry entry : xmlMap.getEntry()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
