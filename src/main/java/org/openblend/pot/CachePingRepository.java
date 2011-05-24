/*
* JBoss, Home of Professional Open Source
* Copyright $today.year Red Hat Inc. and/or its affiliates and other
* contributors as indicated by the @author tags. All rights reserved.
* See the copyright.txt in the distribution for a full listing of
* individual contributors.
* 
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
* 
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.openblend.pot;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@ApplicationScoped
public class CachePingRepository implements PingRepository
{
   private EmbeddedCacheManager manager;
   private Cache<String, PingerInfo> cache;
   private PingerInfo info;

   public EmbeddedCacheManager getManager()
   {
      try
      {
         if (manager == null)
         {
            Context context = new InitialContext();
            try
            {
               manager = (EmbeddedCacheManager) context.lookup("java:jboss/infinispan/web");
            }
            finally
            {
               context.close();
            }
         }
         return manager;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public Cache<String, PingerInfo> getCache()
   {
      if (cache == null)
         cache = getManager().getCache("repl-async", false);

      return cache;
   }

   public PingerInfo getPiger()
   {
      info = getCache().get(PingerInfo.class.getSimpleName());
      if (info == null)
         info = new PingerInfo();

      return info;
   }

   public void updatePinger()
   {
      getCache().put(PingerInfo.class.getSimpleName(), info);
   }
}
