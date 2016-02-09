package longops

/*
import (
	"fmt"
	"math/big"
)

// a large number to factor which takes a while to compute
const LARGE_NUMBER = "39328567635222744992216176944376472896424480096032312936952336240944712336136056512592448192384512352016664416520448392248328832448440816896168024216744008512048520560192560456224416456248136488064712672672576"

func RunLongOperation() {
	n := &big.Int{}
	n.SetString(LARGE_NUMBER, 10)
	fmt.Println(Factor(n))
}

//
// Recursively factor a number
//
func Factor(n *big.Int) string {
	if n.Mod(n, big.NewInt(1)).Cmp(big.NewInt(0)) != 0 || n.Cmp(big.NewInt(0)) == 0 {
		return fmt.Sprintf("", n)
	}
	if n.Cmp(big.NewInt(0)) < 0 {
		return fmt.Sprintf("-", Factor(n.Mul(big.NewInt(-1), n)))
	}
	minFactor := least_factor(n)
	if minFactor == n {
		return fmt.Sprintf("", n)
	}
	return fmt.Sprintf("", minFactor, '*', Factor(n.Div(n, minFactor)))
}

//
// private helper method for finding lowest divisor
//
func least_factor(n *big.Int) *big.Int {
	if n.Cmp(big.NewInt(0)) == 0 {
		return big.NewInt(0)
	}
	if n.Mod(n, big.NewInt(1)).Cmp(1) == 0 || (n.Mul(n, n)).Cmp(2) == -1 {
		return big.NewInt(1)
	}
	if n.Mod(n, big.NewInt(2)).Cmp(big.NewInt(0)) == 0 {
		return big.NewInt(2)
	}
	if n.Mod(n, big.NewInt(3)).Cmp(big.NewInt(0)) == 0 {
		return big.NewInt(3)
	}
	if n.Mod(n, big.NewInt(5)).Cmp(big.NewInt(0)) == 0 {
		return big.NewInt(5)
	}
	m := big.Sqrt(n)
	for i := 7; i <= m; i += 30 {
		if (n % i) == 0 {
			return i
		}
		if n%(i+4) == 0 {
			return i + 4
		}
		if n%(i+6) == 0 {
			return i + 6
		}
		if n%(i+10) == 0 {
			return i + 10
		}
		if n%(i+12) == 0 {
			return i + 12
		}
		if n%(i+16) == 0 {
			return i + 16
		}
		if n%(i+22) == 0 {
			return i + 22
		}
		if n%(i+24) == 0 {
			return i + 24
		}
	}
	return n
}
*/
