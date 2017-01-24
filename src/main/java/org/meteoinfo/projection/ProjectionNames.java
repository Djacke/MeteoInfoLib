/* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteoinfo.projection;

/**
 *
 * @author Yaqiang Wang
 */
public enum ProjectionNames {
    /// <summary>
        /// lon/lat
        /// </summary>
        LongLat,
        /// <summary>
        /// Lambert conformal conic
        /// </summary>
        Lambert_Conformal_Conic,
        /// <summary>
        /// Lambert azimuthal equal area
        /// </summary>
        Lambert_Azimuthal_Equal_Area,
        //Lambert_Equal_Area_Conic,
        /// <summary>
        /// Albers conic equal area
        /// </summary>
        Albers_Equal_Area,
        /// <summary>
        /// Northe polar stereographic
        /// </summary>
        North_Polar_Stereographic_Azimuthal,
        /// <summary>
        /// South polar stereographic
        /// </summary>
        South_Polar_Stereographic_Azimuthal,
        /// <summary>
        /// Mercator
        /// </summary>
        Mercator,
        /// <summary>
        /// Robinson
        /// </summary>
        Robinson,
        /// <summary>
        /// Mollweide
        /// </summary>
        Molleweide,
        /// <summary>
        /// Orthographic
        /// </summary>
        Orthographic_Azimuthal,
        /// <summary>
        /// Geostationary
        /// </summary>
        Geostationary_Satellite,
        /// <summary>
        /// Oblique stereographic
        /// </summary>        
        Oblique_Stereographic_Alternative,
        /// <summary>
        /// Transverse mercator
        /// </summary>
        Transverse_Mercator,
        /// <summary>
        /// Hotine_Oblique_Mercator
        /// </summary>
        //Hotine_Oblique_Mercator,
        /// <summary>
        /// Universal transverse mercator
        /// </summary>
        //Universal_Transverse_Mercator
        Sinusoidal,
        Cylindrical_Equal_Area
}
