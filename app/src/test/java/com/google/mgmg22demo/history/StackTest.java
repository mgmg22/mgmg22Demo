package com.google.mgmg22demo.history;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * 判断括号()[]{}是否成对出现
 */
public class StackTest {

    @Test
    public void testIsCorrectExpressionParen() {
        System.out.println("判断括号()[]{}是否成对出现=" + isCorrectExpressionParen("{}{}{}[]"));
    }

    /**
     * 映射右括号和左括号
     */
    private final static Map<Character, Character> PAREN_DICT_MAP = new HashMap<>(4);
    /**
     * 所有的左括号放在一个集合中
     */
    private final static Set<Character> LEFT_PAREN_SET;

    static {
        PAREN_DICT_MAP.put(')', '(');
        PAREN_DICT_MAP.put(']', '[');
        PAREN_DICT_MAP.put('}', '{');
        LEFT_PAREN_SET = new HashSet<>(PAREN_DICT_MAP.values());
    }

    /**
     * 判断表达式 括号使用是否正确
     *
     * @param expression 表达式
     * @return boolean
     */
    public static boolean isCorrectExpressionParen(String expression) {
        if (expression.isEmpty()) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        char[] chars = expression.toCharArray();
        for (char c : chars) {
            if (LEFT_PAREN_SET.contains(c)) {
                //如果是左括号，直接入栈
                stack.push(c);
                continue;
            }
            //如果是右括号，则弹出栈顶的左括号进行比较，看是否是一对
            if (PAREN_DICT_MAP.containsKey(c)) {
                if (stack.isEmpty() || !stack.pop().equals(PAREN_DICT_MAP.get(c))) {
                    return false;
                }
            }
        }
        //如果最后栈不为空，说明表达式也不正确
        return stack.isEmpty();
    }
}

