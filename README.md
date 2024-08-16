# GRAFCET-syntax-analysis
Syntactic analysis of IEC 60848 GRAFCET-instances based on a [meta-model](https://github.com/Project-AGRAFE/GRAFCET-meta-model).


# Installation
- The provided code is an Eclipse plug-in project and depends on the GRAFCET-editor provided [here](https://github.com/Project-AGRAFE/GRAFCET-editor).
- Set up the GRAFCET editor as explained [here](https://github.com/Project-AGRAFE/GRAFCET-editor).
- Import the projects from this repository into the same Eclipse workspace as the GRAFCET-editor.

# Run the analysis
- Run the editor by creating a new runtime using _Run Configurations --> Eclipse Application_.
- Create a .grafcet file or [import one] (https://github.com/Project-AGRAFE/GRAFCET-instances).
- Right-klick the .grafcet file and choose _GRAFCET --> Check Syntax_ to run the analysis.
- A folder will be crated containing .txt files providing analysis results.

# Publications
Rule based analysis is published in:
  
@INPROCEEDINGS{Julius19,
  author={Julius, Robert and Trenner, Thomas and Fay, Alexander and Neidig, Joerg and Hoang, Xuan Luu},
  booktitle={2019 IEEE International Systems Conference (SysCon)}, 
  title={A meta-model based environment for GRAFCET specifications}, 
  year={2019},
  volume={},
  number={},
  pages={1-7},
  doi={10.1109/SYSCON.2019.8836959}}

Remaining analyses are published in:

_tbd_
