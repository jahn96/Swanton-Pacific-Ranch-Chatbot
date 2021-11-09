import os, copy
from csv import reader
from IPython.display import clear_output
from parlai.scripts.train_model import TrainModel
from parlai.scripts.interactive import Interactive
from parlai.scripts.display_data import DisplayData
from parlai.scripts.display_model import DisplayModel
from parlai.core.teachers import register_teacher, DialogTeacher, ParlAIDialogTeacher


# Get data
worksheet_name = 'SPR QA Training Data'

# read tsv file as a list of lists
with open(worksheet_name + '.tsv', 'r') as read_obj:
    # pass the file object to reader() to get the reader object
    csv_reader = reader(read_obj, delimiter='\t')
    # Pass reader object to list() to get a list of lists
    rows = list(csv_reader)

# Convert data to ParlAI Dialogue format
with open(worksheet_name + ".txt", 'w') as f:
  for j in range(len(rows)):
    for i in range(0,len(rows[j]),2):
      if rows[j][i] == "":
        break
      if j > 0:
        f.write("\n")
      f.write("text:")
      f.write(rows[j][i])
      f.write("\t")
      f.write("labels:")
      if i < len(rows[j]):
        f.write(rows[j][i+1])
    f.write("\tepisode_done:True")
