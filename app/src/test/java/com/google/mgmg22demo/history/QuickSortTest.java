package com.google.mgmg22demo.history;

import org.junit.Test;

/**
 * 快速排序是一种排序执行效率很高的排序算法，它利用分治法来对待排序序列进行分治排序，
 * 它的思想主要是通过一趟排序将待排记录分隔成独立的两部分，其中的一部分比关键字小，后面一部分比关键字大，
 * 然后再对这前后的两部分分别采用这种方式进行排序，通过递归的运算最终达到整个序列有序
 * 时间复杂度为n*logn
 *
 * @Author shenxiaoshun
 * @Date 2021/3/9
 */
public class QuickSortTest {

    @Test
    public void test() {
        int[] test = new int[]{3, 1, 3, 5, 6, 7, 9, 13, 11};
        quickSort(test);
        for (int a : test) {
            System.out.println(a + "");
        }
    }

    public void quickSort(int[] array) {
        int len;
        if (array == null
                || (len = array.length) == 0
                || len == 1) {
            return;
        }
        sort(array, 0, len - 1);
    }

    /**
     * 快排核心算法，递归实现
     *
     * @param array
     * @param left
     * @param right
     */
    public void sort(int[] array, int left, int right) {
        if (left > right) {
            return;
        }
        // base中存放基准数
        int base = array[left];
        int i = left, j = right;
        while (i != j) {
            // 顺序很重要，先从右边开始往左找，直到找到比base值小的数
            while (array[j] >= base && i < j) {
                j--;
            }
            // 再从左往右边找，直到找到比base值大的数
            while (array[i] <= base && i < j) {
                i++;
            }
            // 上面的循环结束表示找到了位置或者(i>=j)了，交换两个数在数组中的位置
            if (i < j) {
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
        // 将基准数放到中间的位置（基准数归位）
        array[left] = array[i];
        array[i] = base;
        // 递归，继续向基准的左右两边执行和上面同样的操作
        // i的索引处为上面已确定好的基准值的位置，无需再处理
        sort(array, left, i - 1);
        sort(array, i + 1, right);
    }
}
