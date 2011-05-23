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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Date;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class PingServlet extends HttpServlet
{
   @Inject
   private PingerInfo info;

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      if (req.getParameter("cleanup") != null)
         info.setName(null);

      String name = info.getName();
      if (name == null)
      {
         name = req.getParameter("name");
         info.setName(name);
      }

      System.out.println("Name: " + name);
      System.out.println("Pinger: " + info);

      InetAddress address = InetAddress.getLocalHost();
      StringBuilder buf = new StringBuilder();
      buf.append("Name: ").append(name).append(" - ").append(address).append(" @ ").append(new Date());

      OutputStream stream = resp.getOutputStream();
      stream.write(buf.toString().getBytes());
      stream.flush();
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      service(req, resp);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      service(req, resp);
   }
}
