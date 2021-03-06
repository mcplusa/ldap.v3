// Copyright 2011 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.connector.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.enterprise.connector.util.diffing.SnapshotRepositoryRuntimeException;

import org.junit.Test;

/**
 * Tests for implementations of IllegalStateException handling in JsonDocumentFetcher iterator.
 */
public class JsonDocumentFetcherIteratorTest {
  @Test
  public void testIterator() {
    int[] waitTimer = { 200, 500 };
    JsonDocumentFetcher jdf =
        new LdapJsonDocumentFetcher(MockLdapHandlers.getExceptionMock(), waitTimer);

    for (int i = 0; i < waitTimer.length; i++) {
      long beforeTime = System.currentTimeMillis();
      try {
        jdf.iterator();
        fail("Expected a SnapshotRepositoryRuntimeException");
      } catch (SnapshotRepositoryRuntimeException expected) {
      }
      long afterTime = System.currentTimeMillis();
      assertEquals(waitTimer[i], (afterTime - beforeTime), 100);
    }

    // run again just to verify that it only waits for 500 milliseconds ( the last
    // wait time in array ).
    for (int i = 0; i < 2; i++) {
      long beforeTime = System.currentTimeMillis();
      try {
        jdf.iterator();
        fail("Expected a SnapshotRepositoryRuntimeException");
      } catch (SnapshotRepositoryRuntimeException expected) {
      }
      long afterTime = System.currentTimeMillis();
      assertEquals(waitTimer[waitTimer.length - 1], (afterTime - beforeTime), 100);
    }
  }
}
