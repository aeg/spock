/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spockframework.smoke.condition

import spock.lang.*
import static spock.lang.Predef.*
import org.junit.runner.RunWith

import static org.spockframework.smoke.condition.ConditionSpeckUtil.*
import static java.lang.Thread.State.*

/**
 * Describes rendering of individual values.
 *
 * @author Peter Niederwieser
 */

@Speck
@RunWith (Sputnik)
class ValueRendering {
  def "null value"() {
    expect:
    isRendered """
x
|
null
    """, {
      def x = null
      assert x
    }
  }

  def "char value"() {
    expect:
    isRendered """
x == null
| |
c false
    """, {
      def x = "c" as char
      assert x == null
    }
  }

  def "string value"() {
    expect:
    isRendered """
x == null
| |
| false
foo
    """, {
      def x = "foo"
      assert x == null
    }
  }

  @Issue("17")
  def "empty string value"() {
    isRendered """
x == null
| |
| false
""
    """, {
      def x = ""
      assert x == null
    }
  }

  def "primitive array value"() {
    expect:
    isRendered """
x == null
| |
| false
[1, 2]
    """, {
      def x = [1, 2] as int[]
      assert x == null
    }
  }

  def "object array value"() {
    expect:
    isRendered """
x == null
| |
| false
[one, two]
    """, {
      def x = ["one", "two"] as String[]
      assert x == null
    }
  }

  def "single-line toString"() {
    expect:
    isRendered """
x == null
| |
| false
single line
    """, {
      def x = new SingleLineToString()
      assert x == null
    }
  }

  def "multi-line toString"() {
    expect:
    isRendered """
x == null
| |
| false
mul
tiple
   lines
    """, {
      def x = new MultiLineToString()
      assert x == null
    }
  }

  def "null toString"() {
    expect:
    def x = new NullToString()

    isRendered """
x == null
| |
| false
${x.objectToString()}
    """, {
      assert x == null
    }
  }

  def "empty toString"() {
    def x = new EmptyToString()

    expect:
    isRendered """
x == null
| |
| false
${x.objectToString()}
    """, {
      assert x == null
    }
  }

  def "exception-throwing toString"() {
    def x = new ThrowingToString()

    expect:
    isRendered """
x == null
| |
| false
${x.objectToString()} (DGM.toString() threw java.lang.UnsupportedOperationException)
    """, {
      assert x == null
    }
  }

  def "enum literal"() {
    expect:
    isRendered """
Thread.State.NEW == null
                 |
                 false
    """, {
      assert Thread.State.NEW == null
    }
  }

  def "statically imported enum literal"() {
    expect:
    isRendered """
NEW == null
    |
    false
    """, {
      assert NEW == null
    }
  }

  def "enum literal with toString"() {
    expect:
    isRendered """
EnumWithToString.VALUE == null
                 |     |
                 |     false
                 I'm a value
    """, {
      assert EnumWithToString.VALUE == null
    }
  }

  def "variable with enum value"() {
    expect:
    isRendered """
x == null
| |
| false
NEW
    """, {
      def x = NEW
      assert x == null
    }
  }
}

private class SingleLineToString {
  String toString() {
    "single line"
  }
}

private class MultiLineToString {
  String toString() {
    "mul\ntiple\n   lines"
  }
}

private class NullToString {
  String objectToString() {
    super.toString()
  }

  String toString() { null }
}

private class EmptyToString {
  String objectToString() {
    super.toString()
  }

  String toString() { "" }
}

private class ThrowingToString {
  String objectToString() {
    super.toString()
  }

  String toString() {
    throw new UnsupportedOperationException()
  }
}

private enum EnumWithToString {
  VALUE
  String toString() { "I'm a value" }
}