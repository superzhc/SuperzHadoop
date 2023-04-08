import unittest
import akshare as ak

class TestAKShare(unittest.TestCase):

    def test_data(self):
        """
        未执行，FIXME
        :return:
        """
        stock_zh_a_hist_df = ak.stock_zh_a_hist(symbol="000001", period="daily", start_date="20170301", end_date='20210907', adjust="")
        self.assertEqual(True,True)
        print(stock_zh_a_hist_df)

    def test_plot(self):
        pass


if __name__ == '__main__':
    unittest.main()
