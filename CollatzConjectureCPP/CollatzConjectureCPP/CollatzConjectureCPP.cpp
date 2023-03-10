#include <iostream>
#include <cstdint>
#include <ctime>
#include <thread>
#include <vector>

using namespace std;

    void loop(int64_t id, int64_t multi) {
        cout << id;

        uint64_t i;
        uint64_t n;
        uint64_t count;
        uint64_t n_max = 0, n_max_jumps = 0;
        uint64_t work = 1000000;                //This detirmines how much work (numbers) a single procesor thread will process in one go

        uint64_t start = work * id + work * multi;
        uint64_t end = work * (id + 1) + work * multi;

        for (i = start; i < end; i++) {
            n = i;
            count = 0;
            while (n > 1) {
                if (n % 2 == 0) {
                    n /= 2;
                }
                else {
                    n = 3 * n + 1;
                }
                count++;
            }
            if (count > n_max_jumps) {
                n_max = i;
                n_max_jumps = count;
            }
        }

        cout << "Thread: #" << id << ": Number " << n_max << " with " << n_max_jumps << " jumps." << endl;
    }

    int main() {
        int64_t i, j;
        int64_t limiti = 2, limitj = 4;             //limiti detrmines how many times threads will be given work, while limitj detrmines how many threads will be given work
        time_t before = clock();

        vector<thread> vt;
        for (i = 0; i < limiti; i++) {

            for (j = 0; j < limitj; j++) {
                vt.push_back(thread(loop, j, i));
            }
            for (thread& t : vt) {
                if (t.joinable()) {
                    t.join();
                }
            }
            time_t after = clock();
            uint32_t ms = (after - before) * 1000 / CLOCKS_PER_SEC;
            cout << ms << " ms" << endl;
        }
        return 0;
    }