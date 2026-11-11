import os 

  

def generate(name, f0, depth, width, lo, hi, step, floor=-0.6): 

    n = int(round((hi - lo) / step)) + 1 

    os.makedirs("datasets", exist_ok=True) 

    with open(f"datasets/{name}.csv", "w") as fh: 

        fh.write(f"# Antenna measurement dataset: {name}\n") 

        fh.write("# frequency_GHz,s11_dB\nfrequency_GHz,s11_dB\n") 

        for i in range(n): 

            f = lo + i * step 

            s = floor - depth / (1 + ((f - f0) / width) ** 2) 

            fh.write(f"{f:.3f},{s:.2f}\n") 

  

generate("patch_2p4GHz",       2.45,  26, 0.045, 2.20, 2.70, 0.01) 

generate("monopole_wideband",  3.50,  22, 0.400, 2.50, 4.50, 0.05) 

generate("dipole_915MHz",      0.915, 14, 0.012, 0.85, 0.98, 0.005) 

generate("poor_match_5p8GHz",  5.80,   5, 0.150, 5.00, 6.50, 0.05, floor=-0.8) 

 