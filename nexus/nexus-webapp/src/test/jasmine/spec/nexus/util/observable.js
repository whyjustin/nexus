/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
define(['nexus/util/observable'], function(Observable) {
  describe('Nexus.util.Observable', function() {

    var underTest;
    var event;
    var handler;

    beforeEach(function() {
      underTest = new Observable();
      event = {};
      handler = function(arg) {
        event.arg = arg;
      };
    });

    it('should be defined', function() {
      expect(underTest).toBeTruthy();
    });


    it('should be able to add listeners', function() {
      underTest.addListener('test', handler);
      underTest.fireEvent('test', 'arg');
      expect(event.arg).toBe('arg');
    });

    it('should not call listeners not registered for the event', function() {
      underTest.addListener('test', handler);
      underTest.fireEvent('anothertest', 'arg');
      expect(event.arg).toBeUndefined();
    });
  });
});